package com.zs.sms.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 短信记录表
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
@Getter
@Setter
@Schema(description = "短信记录updateParams对象")
public class SysSmsRecordUpdateParams implements Serializable {


    @Schema(description = "表ID")
    @NotNull(message = "表ID不能为空")
    private Long sysSmsRecordId;

    @Schema(description = "请求ID")
    @NotBlank(message = "请求ID不能为空")
    private String requestId;

    @Schema(description = "接收短信手机号")
    @NotBlank(message = "接收短信手机号不能为空")
    private String phoneNumbers;

    @Schema(description = "短信内容")
    @NotBlank(message = "短信内容不能为空")
    private String content;

    @Schema(description = "短信模板ID")
    @NotBlank(message = "短信模板ID不能为空")
    private String templateCode;

    @Schema(description = "模板变量")
    @NotBlank(message = "模板变量不能为空")
    private String templateParams;

    @Schema(description = "短信通道/服务商")
    @NotNull(message = "短信通道/服务商不能为空")
    private Long channel;

    @Schema(description = "发送状态：0-待发送，1-发送中，2-成功，3-失败，4-取消")
    @NotNull(message = "发送状态：0-待发送，1-发送中，2-成功，3-失败，4-取消不能为空")
    private Long status;

    @Schema(description = "发送时间")
    @NotNull(message = "发送时间不能为空")
    private Date sendTime;


}
