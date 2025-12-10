package com.zs.common.core.model.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "阿里云")
public class Aliyun {



    @Schema(description = "阿里云accessKeyId")
    private String accessKeyId;

    @Schema(description = "阿里云accessKeySecret")
    private String accessKeySecret;

    @Schema(description = "Endpoint")
    private String endpoint;
}
