package com.zs.file.strategy;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.core.model.file.Tencent;
import com.zs.file.domain.entity.SysFileEntity;
import com.zs.file.manager.COSClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 *
 */
@Slf4j
public class TencentCloudStrategy implements UploadStrategy {


    private final COSClient cosClient;
    private final String bucketName;
    private final String domain;

    public TencentCloudStrategy(SysConfigFileVO sysConfigFileVO) {
        if (sysConfigFileVO == null || sysConfigFileVO.getTencent() == null) {
            throw new IllegalArgumentException("腾讯云配置不能为空");
        }
        Tencent tencent = sysConfigFileVO.getTencent();
        if (tencent.getSecretId() == null || tencent.getSecretKey() == null || tencent.getRegion() == null) {
            throw new IllegalArgumentException("腾讯云配置信息不完整");
        }

        this.cosClient = COSClientManager.getCOSClient(tencent);
        this.bucketName = tencent.getBucketName();
        this.domain = tencent.getDomain();
    }

    @Override
    public SysFileEntity upload(MultipartFile file) {
        File localFile = null;
        try {
            // 创建临时文件
            localFile = File.createTempFile("temp", file.getOriginalFilename());
            // 将 MultipartFile 写入临时文件
            file.transferTo(localFile);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getOriginalFilename(), localFile);
            PutObjectResult result = cosClient.putObject(putObjectRequest);
            SysFileEntity sysFileEntity = new SysFileEntity();
            sysFileEntity.setFileName(file.getOriginalFilename());
            sysFileEntity.setFileOriginalName(file.getOriginalFilename());
            sysFileEntity.setFileType(file.getContentType());
            sysFileEntity.setFileSize(file.getSize());
            sysFileEntity.setFileUrl(domain + "/" + file.getOriginalFilename());
            sysFileEntity.setFilePath(result.getRequestId());

            // 删除临时文件（可选）
            localFile.deleteOnExit();

            return sysFileEntity;
        }catch (Exception e) {
            log.error("上传文件失败",e);
        }finally {
            if (localFile != null && localFile.exists()) {
                // 删除临时文件（可选）
                localFile.deleteOnExit();
            }
        }
        return new SysFileEntity();
    }

    @Override
    public List<SysFileEntity> uploads(MultipartFile[] files) {
        return List.of();
    }
}
