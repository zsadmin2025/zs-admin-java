package com.zs.mail.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zs
 * @since 2025-10-30 10:44:53
 */
@Getter
@Setter
@Schema(description = "邮件任务VO对象")
public class SysMailTasksVO implements Serializable {

    @Schema(description = "邮件任务表ID")
    private Long sysMailTasksId;

    @Schema(description = "邮件主题")
    private String subject;

    @Schema(description = "邮件正文（HTML或者纯文本）")
    private String content;

    @Schema(description = "发件人邮箱地址")
    private String sender;

    @Schema(description = "发件人姓名")
    private String senderName;

    @Schema(description = "收件人列表")
    private List<String> recipients = List.of();

    @Schema(description = "抄送列表")
    private List<String> cc = List.of();

    @Schema(description = "密送列表")
    private List<String> bcc = List.of();

    @Schema(description = "使用的邮件模板表ID(可选)")
    private Long templateId;

    @Schema(description = "状态，1-草稿 2-已发送")
    private Integer status;

    @Schema(description = "创建时间")
    private String createTime;

}
