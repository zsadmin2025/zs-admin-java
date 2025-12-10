package com.zs.mail.domain.params;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "邮件任务AddParams对象")
public class SysMailTasksAddParams implements Serializable {

    @Schema(description = "邮件主题")
    @NotBlank(message = "邮件主题不能为空")
    private String subject;

    @Schema(description = "邮件正文（HTML或者纯文本）")
    @NotBlank(message = "邮件正文不能为空")
    private String content;

    @Schema(description = "收件人列表")
    @NotBlank(message = "收件人不能为空")
    private List<String> recipients = List.of();

    @Schema(description = "抄送列表")
    @NotBlank(message = "抄送不能为空")
    private List<String> cc = List.of();

    @Schema(description = "密送列表")
    @NotBlank(message = "密送不能为空")
    private List<String> bcc = List.of();

    @Schema(description = "使用的邮件模板表ID")
    @NotNull(message = "使用的邮件模板表ID不能为空")
    private Long templateId;

    @Schema(description = "状态，1-草稿 2-已发送")
    @NotNull(message = "状态不能为空")
    private Integer status;



}
