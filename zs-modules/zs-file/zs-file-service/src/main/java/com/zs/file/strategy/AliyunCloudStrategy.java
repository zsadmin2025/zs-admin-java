package com.zs.file.strategy;

import com.aliyun.oss.OSS;

import com.aliyun.sdk.service.oss2.OSSAsyncClient;
import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.models.PutObjectResult;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import com.zs.common.core.model.file.Aliyun;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.common.core.utils.FileUtils;
import com.zs.file.domain.entity.SysFileEntity;
import com.zs.file.manager.OSSAsyncClientManager;
import com.zs.file.manager.OSSClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class AliyunCloudStrategy implements UploadStrategy  {


    private final OSSAsyncClient ossClient;
    private final String bucketName;
    private final String domain;

    public AliyunCloudStrategy(SysConfigFileVO sysConfigFileVO) {
        if (sysConfigFileVO == null || sysConfigFileVO.getAliyun() == null) {
            throw new IllegalArgumentException("阿里云配置不能为空");
        }
        Aliyun aliyun = sysConfigFileVO.getAliyun();
        if (aliyun.getAccessKeyId() == null || aliyun.getAccessKeySecret() == null || aliyun.getEndpoint() == null) {
            throw new IllegalArgumentException("阿里云配置信息不完整");
        }

        this.ossClient = OSSAsyncClientManager.getOSSClient(aliyun);
        this.bucketName = aliyun.getBucketName();
        this.domain = aliyun.getDomain();
    }



    @Override
    public SysFileEntity upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        log.info("上传文件开始，文件名：{}", file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()){
            String objectKey = FileUtils.generateObjectKey(originalFilename);
            PutObjectResult result = ossClient.putObjectAsync(PutObjectRequest.newBuilder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .body(BinaryData.fromStream(inputStream))
                    .contentType(file.getContentType())
                    .build())
                    .get(30, TimeUnit.SECONDS);// 超时控制，避免无限阻塞


            SysFileEntity sysFileEntity = new SysFileEntity();
            sysFileEntity.setFileName(file.getOriginalFilename());
            sysFileEntity.setFileOriginalName(file.getOriginalFilename());
            sysFileEntity.setFileType(file.getContentType());
            sysFileEntity.setFileSize(file.getSize());
            sysFileEntity.setFileUrl(domain + "/" + file.getOriginalFilename());
            sysFileEntity.setFilePath(result.requestId());
            log.info("上传文件结束，文件名：{}", file.getOriginalFilename());
            return sysFileEntity;
        }catch (Exception e) {
            log.error("上传文件失败，文件名：{}", file.getOriginalFilename(), e);
        }

        return null;
    }


    @Override
    public List<SysFileEntity> uploads(MultipartFile[] files) {
        return List.of();
    }
}
