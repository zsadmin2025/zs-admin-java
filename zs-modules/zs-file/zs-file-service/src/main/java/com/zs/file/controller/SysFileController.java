package com.zs.file.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.core.core.Result;
import com.zs.file.domain.entity.SysFileEntity;
import com.zs.file.domain.vo.SysFileVO;
import com.zs.file.factory.FileFactory;
import com.zs.file.service.ISysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 *
 */
@RestController
@RequestMapping("/system/file")
@Tag(name = "附件管理", description = "附件管理" )
public class SysFileController {

    @Resource
    private ISysFileService sysFileService;


    @Operation(summary = "附件上传")
    @Parameters({
            @Parameter(name = "file", description = "文件", required = true),
    })
    @PostMapping("/upload")
    public Result<SysFileVO> upload( @RequestParam("file") MultipartFile file) {
        // 判断文件是否为空
        if (file.isEmpty()) {
            return new Result<>(500, "上传失败，请选择文件");
        }

        try {
            //根据不同的策略选择不同的上传方式
            SysFileEntity sysFileEntity = Objects.requireNonNull(FileFactory.build()).upload(file);
            // 保存到数据库
            sysFileService.save(sysFileEntity);


            if (sysFileEntity == null) {
                return new Result<>(500, "上传失败");
            }
            return new Result<>(200, "上传成功", BeanUtil.copyProperties(sysFileEntity, SysFileVO.class));
        }catch (Exception e) {
            return new Result<>(500, "上传失败");
        }
    }

    @Operation(summary = "多附件上传")
    @Parameters({
            @Parameter(name = "file", description = "文件", required = true, array = @ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = MultipartFile.class))),
    })
    @PostMapping("/uploads")
    public Result<List<SysFileVO>> uploads(@RequestParam("file") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return new Result<>(500, "上传失败，请选择文件");
        }
        try {
            //根据不同的策略选择不同的上传方式
            List<SysFileEntity> sysFileEntities = Objects.requireNonNull(FileFactory.build()).uploads(files);
            // 保存到数据库
            sysFileService.saveBatch(sysFileEntities);

            if (sysFileEntities.isEmpty()) {
                return new Result<>(500, "上传失败");
            }
            return new Result<>(200, "上传成功", BeanUtil.copyToList(sysFileEntities, SysFileVO.class));
        }catch (Exception e) {
            return new Result<>(500, "上传失败");
        }
    }



}
