package com.zs.file.strategy;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.zs.common.core.model.file.Aliyun;
import com.zs.common.core.model.file.SysConfigFileVO;
import com.zs.file.domain.entity.SysFileEntity;
import com.zs.file.manager.OSSClientManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 */

public class AliyunCloudStrategy implements UploadStrategy  {


    private final OSS ossClient;
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

        this.ossClient = OSSClientManager.getOSSClient(aliyun);
        this.bucketName = aliyun.getBucketName();
        this.domain = aliyun.getDomain();
    }



    @Override
    public SysFileEntity upload(MultipartFile file) {
        try{
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, file.getOriginalFilename(), file.getInputStream());
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            SysFileEntity sysFileEntity = new SysFileEntity();
            sysFileEntity.setFileName(file.getOriginalFilename());
            sysFileEntity.setFileOriginalName(file.getOriginalFilename());
            sysFileEntity.setFileType(file.getContentType());
            sysFileEntity.setFileSize(file.getSize());
            sysFileEntity.setFileUrl(domain + "/" + file.getOriginalFilename());
            sysFileEntity.setFilePath(result.getRequestId());

            return sysFileEntity;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<SysFileEntity> uploads(MultipartFile[] files) {
        return List.of();
    }
}
