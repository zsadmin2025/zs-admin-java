package com.zs.common.core.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "华为云")
public class Huawei {

    @Schema(description = "访问域名")
    private String domain;

    @Schema(description = "所属地域")
    private String endPoint;

    @Schema(description = "accessKey")
    private String accessKey;

    @Schema(description = "secretAccessKey")
    private String secretAccessKey;

    @Schema(description = "存储桶名称")
    private String bucketName;
}
