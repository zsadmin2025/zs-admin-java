package com.zs.sys.config.domain.vo;

import config.dto.sms.Aliyun;
import config.dto.sms.Tencent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Schema(description = "系统配置-短信")
@Data
public class SysConfigSmsVO {

    @Schema(description = "短信服务商类型")
    private Integer type;

    @Schema(description = "阿里云配置")
    private Aliyun aliyun;

    @Schema(description = "腾讯云配置")
    private Tencent tencent;
}
