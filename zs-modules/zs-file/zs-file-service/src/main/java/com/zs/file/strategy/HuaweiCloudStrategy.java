package com.zs.file.strategy;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import com.zs.common.core.model.file.Huawei;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.core.utils.FileUtils;
import com.zs.file.domain.entity.SysFileEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


/**
 * 华为云 文件上传策略
 */
@Slf4j
public class HuaweiCloudStrategy implements UploadStrategy {

    private final ObsClient obsClient;
    private final String bucketName;
    private final String domain;

    public HuaweiCloudStrategy(SysConfigFileVO sysConfigFileVO) {
        if (sysConfigFileVO == null || sysConfigFileVO.getHuawei() == null) {
            throw new IllegalArgumentException("华为云配置不能为空");
        }
        Huawei huawei = sysConfigFileVO.getHuawei();
        if (huawei.getBucketName() == null || huawei.getBucketName().isEmpty()) {
            throw new IllegalArgumentException("华为云存储桶名称不能为空");
        }

        this.obsClient = new ObsClient(huawei.getAccessKey(), huawei.getSecretAccessKey(), huawei.getEndPoint());
        this.bucketName = huawei.getBucketName();
        this.domain = huawei.getDomain();

    }


    @Override
    public SysFileEntity upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        File localFile = null;
        try {
            String objectKey = FileUtils.generateObjectKey(originalFilename);
            // 创建临时文件
            localFile = File.createTempFile("temp", originalFilename);
            // 将 MultipartFile 写入临时文件
            file.transferTo(localFile);

            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);
            request.setFile(localFile);
            PutObjectResult result = obsClient.putObject(request);

            SysFileEntity sysFileEntity = new SysFileEntity();
            sysFileEntity.setFileName(originalFilename);
            sysFileEntity.setFileOriginalName(originalFilename);
            sysFileEntity.setFileType(file.getContentType());
            sysFileEntity.setFileSize(file.getSize());
            sysFileEntity.setFileUrl(domain + "/" + objectKey);
            sysFileEntity.setFilePath(result.getRequestId());

            return sysFileEntity;

        } catch (Exception e) {
            log.error("上传文件失败", e);
        } finally {
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
