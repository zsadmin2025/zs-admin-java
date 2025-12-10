package config.dto.file;

import lombok.Data;

/**
 * 阿里云
 */
@Data
public class Aliyun {

    /**
     * 文件访问域名。
     */
    private String domain;

    /**
     * 文件上传地址。
     */
    private String endpoint;

    /**
     * 阿里云accessKeyId。
     */
    private String accessKeyId;

    /**
     * 阿里云accessKeySecret。
     */
    private String accessKeySecret;

    /**
     * 阿里云bucketName。
     */
    private String bucketName;
}
