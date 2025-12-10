package com.zs.common.core.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
@Schema(description = "腾讯云")
public class Tencent {

    @Schema(description = "访问域名")
    private String domain;

    @Schema(description = "所属地域")
    private String region;

    @Schema(description = "secretId")
    private String secretId;

    @Schema(description = "secretKey")
    private String secretKey;

    @Schema(description = "存储桶名称")
    private String bucketName;
}
