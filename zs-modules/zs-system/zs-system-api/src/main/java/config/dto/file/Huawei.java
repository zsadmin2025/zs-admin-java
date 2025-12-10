package config.dto.file;

import lombok.Data;

/**
 * 华为云
 */
@Data
public class Huawei {

    /**
     * 访问域名
     */
    private String domain;

    /**
     * 存储桶名称
     */
    private String endPoint;

    /**
     * 密钥
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretAccessKey;

    /**
     * 存储桶名称
     */
    private String bucketName;
}
