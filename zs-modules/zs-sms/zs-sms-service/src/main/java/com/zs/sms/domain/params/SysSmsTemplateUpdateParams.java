package com.zs.sms.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 短信模板表
 * </p>
 *
 * @author zs
 * @since 2025-11-26 09:40:35
 */
@Getter
@Setter
@Schema(description = "短信模板updateParams对象")
public class SysSmsTemplateUpdateParams implements Serializable {


    @Schema(description = "短信模板表id")
    private Long sysSmsTemplateId;

    @Schema(description = "模板编号")
    private String templateNumber;

    @Schema(description = "短信通道/服务商")
    private Long channel;

    @Schema(description = "短信签名")
    private String signName;

    @Schema(description = "短信模板code")
    private String templateCode;

    @Schema(description = "短信模板内容")
    private String templateContent;



}
