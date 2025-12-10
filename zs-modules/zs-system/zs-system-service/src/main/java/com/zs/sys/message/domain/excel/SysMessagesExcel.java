package com.zs.sys.message.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-11-17 09:01:44
 */
@Getter
@Setter
@Schema(description = "消息Excel对象")
@ExcelIgnoreUnannotated
public class SysMessagesExcel {

    @ExcelProperty("消息表Id")
    private Long sysMessageId;

    @ExcelProperty("消息类型 1-站内信(私信) 2-通知 3-待办")
    private Integer type;

    @ExcelProperty("发送者ID")
    private Long senderId;

    @ExcelProperty("发送者名称")
    private String senderName;

    @ExcelProperty("接收者ID")
    private Long receiverId;

    @ExcelProperty("接收者名称")
    private String receiverName;

    @ExcelProperty("主题")
    private String title;

    @ExcelProperty("内容")
    private String content;

    @ExcelProperty("是否已读（0: 未读, 1: 已读）")
    private Integer isRead;

    @ExcelProperty("阅读时间")
    private Date readTime;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;



}
