package com.zs.mail.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("sys_mail_tasks")
@Schema(description = "邮件任务Entity对象")
public class SysMailTasksEntity extends BaseEntity {

    /**  邮件任务表ID
     */
    @TableId
    private Long sysMailTasksId;

    /**  邮件主题 */
    private String subject;

    /**  邮件正文（HTML或者纯文本） */
    private String content;

    /**  发件人邮箱地址 */
    private String sender;

    /**  发件人姓名 */
    private String senderName;

    /**  收件人列表 */
    private String recipients;

    /**  抄送列表 */
    private String cc;

    /**  密送列表 */
    private String bcc;

    /**  使用的邮件模板表ID(可选) */
    private Long templateId;

    /**  状态 0-未发送 1-已发送 */
    private Integer status;


}

