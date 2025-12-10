package com.zs.sms.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
@Schema(description = "短信记录VO对象")
public class SysSmsRecordVO implements Serializable {

    @Schema(description = "表ID")
    private Long sysSmsRecordId;

    @Schema(description = "请求ID")
    private String requestId;

    @Schema(description = "回执ID")
    private String bizId;

    @Schema(description = "接收短信手机号")
    private String phoneNumbers;

    @Schema(description = "短信内容")
    private String content;

    @Schema(description = "短信模板ID")
    private String templateCode;

    @Schema(description = "模板变量")
    private String templateParams;

    @Schema(description = "短信通道/服务商")
    private Long channel;

    @Schema(description = "发送状态：0-待发送，1-发送中，2-成功，3-失败，4-取消")
    private Long status;

    @Schema(description = "发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;


}
