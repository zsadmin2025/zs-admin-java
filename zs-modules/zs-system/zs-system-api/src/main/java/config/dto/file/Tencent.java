package config.dto.file;

import lombok.Data;

/**
 *
 */
@Data
public class Tencent {

    /**
     * 域名
     */
    private String domain;

    /**
     * 区域
     */
    private String region;

    /**
     * 密钥ID
     */
    private String secretId;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;
}
