package com.zs.common.core.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
@Schema(description = "阿里云")
public class Aliyun {

    @Schema(description = "文件访问域名")
    private String domain;

    @Schema(description = "文件上传地址")
    private String endpoint;

    @Schema(description = "阿里云accessKeyId")
    private String accessKeyId;

    @Schema(description = "阿里云accessKeySecret")
    private String accessKeySecret;

    @Schema(description = "阿里云bucketName")
    private String bucketName;
}
