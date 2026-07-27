package com.zs.file.controller;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.constant.RedisConstants;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.redis.config.RedisUtil;
import config.dto.SysConfigFileDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件预览/下载（生产终极版 - 修复 Range + 安全增强）
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileDownloadController {

    private static final long PATH_CACHE_TTL_MS = 30_000L;
    private final RedisUtil redisUtil;

    private volatile String cachedBasePath;
    private volatile long cachedBasePathExpireAt;

    public FileDownloadController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @GetMapping("/**")
    public ResponseEntity<Object> download(HttpServletRequest request) {
        String basePath = getBasePath();
        if (basePath == null) {
            log.warn("[文件访问] 文件上传路径未配置");
            return ResponseEntity.notFound().build();
        }

        String filePath = extractFilename(request);
        if (filePath == null || filePath.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Path baseDir = Paths.get(basePath).normalize();
        Path targetFile = baseDir.resolve(filePath).normalize();

        if (!targetFile.startsWith(baseDir)) {
            log.warn("[文件访问] 非法路径拦截：{}", filePath);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Resource resource = new UrlResource(targetFile.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            String rangeHeader = request.getHeader(HttpHeaders.RANGE);
            if (rangeHeader != null) {
                return handleRangeRequest(resource, mediaType, rangeHeader, filePath);
            }

            // 完整文件响应
            return buildFullResponse(resource, mediaType, filePath);
        } catch (Exception e) {
            log.error("[文件访问] 读取文件异常：{}", filePath, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * HTTP Range 请求处理（正确使用 ResourceRegion）
     */
    private ResponseEntity<Object> handleRangeRequest(Resource resource, MediaType mediaType,
                                                      String rangeHeader, String fileName) {
        try {
            List<HttpRange> ranges = HttpRange.parseRanges(rangeHeader);
            if (ranges.isEmpty()) {
                return buildFullResponse(resource, mediaType, fileName);
            }

            // 关键修复：直接返回 ResourceRegion，而不是 getResource()
            ResourceRegion region = ranges.get(0).toResourceRegion(resource);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename*=UTF-8''" + encodeFileName(fileName))
                    .body(region);
        } catch (Exception e) {
            log.warn("[文件访问] Range 请求失败，降级返回完整文件：{}", fileName, e);
            return buildFullResponse(resource, mediaType, fileName);
        }
    }

    /**
     * 构建完整文件响应（包含安全头 + 正确的文件名编码）
     */
    private ResponseEntity<Object> buildFullResponse(Resource resource, MediaType mediaType, String fileName) {
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename*=UTF-8''" + encodeFileName(fileName))
                    .contentType(mediaType)
                    .contentLength(resource.contentLength())
                    .header("X-Content-Type-Options", "nosniff")   // 防 MIME 嗅探
                    .body(resource);
        } catch (Exception e) {
            log.error("[文件访问] 构建响应失败：{}", fileName, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // ================== 路径缓存（保留你的设计） ==================
    private String getBasePath() {
        long now = System.currentTimeMillis();
        if (cachedBasePath != null && now < cachedBasePathExpireAt) {
            return cachedBasePath;
        }
        synchronized (this) {
            if (cachedBasePath != null && now < cachedBasePathExpireAt) {
                return cachedBasePath;
            }
            cachedBasePath = loadBasePathFromRedis();
            cachedBasePathExpireAt = now + PATH_CACHE_TTL_MS;
            return cachedBasePath;
        }
    }

    private String loadBasePathFromRedis() {
        try {
            Object obj = redisUtil.get(RedisConstants.SYS_DICT_CONFIG_KEY + Constants.FILE_UPLOAD);
            if (obj == null) return null;

            if (obj instanceof SysConfigFileVO config) {
                return config.getLocal() == null ? null : config.getLocal().getPath();
            }
            if (obj instanceof SysConfigFileDTO config) {
                return config.getLocal() == null ? null : config.getLocal().getPath();
            }
            if (obj instanceof String jsonStr) {
                SysConfigFileVO config = JSONUtil.toBean(jsonStr, SysConfigFileVO.class);
                return config.getLocal() == null ? null : config.getLocal().getPath();
            }
            log.warn("[文件配置] Redis 配置类型异常：{}", obj.getClass().getName());
            return null;
        } catch (Exception e) {
            log.error("[文件配置] 读取 Redis 失败", e);
            return null;
        }
    }

    private String extractFilename(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String prefix = contextPath + "/file/";
        return requestURI.startsWith(prefix) ? requestURI.substring(prefix.length()) : null;
    }

    /**
     * 文件名编码（空格严格转为 %20，兼容 RFC 5987）
     */
    private String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
        } catch (Exception e) {
            return fileName;
        }
    }

    public synchronized void clearCache() {
        this.cachedBasePath = null;
        this.cachedBasePathExpireAt = 0;
        log.info("[文件配置] 本地缓存已清空");
    }
}