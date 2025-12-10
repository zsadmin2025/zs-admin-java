package com.zs.mail.domain.excel;


import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-10-30 10:44:53
 */
@Getter
@Setter
@Schema(description = "邮件任务Excel对象")
@ExcelIgnoreUnannotated
public class SysMailTasksExcel {

    @ExcelProperty("邮件任务表ID")
    private Long sysMailTasksId;

    @ExcelProperty("邮件主题")
    private String subject;

    @ExcelProperty("邮件正文（HTML或者纯文本）")
    private String content;

    @ExcelProperty("发件人邮箱地址")
    private String sender;

    @ExcelProperty("发件人姓名")
    private String senderName;

    @ExcelProperty("收件人")
    private String recipients;

    @ExcelProperty("抄送")
    private String cc;

    @ExcelProperty("密送")
    private String bcc;

    @ExcelProperty("使用的邮件模板表ID(可选)")
    private Long templateId;

    @ExcelProperty(value = "状态")
    private Integer status;

}

