package com.zs.sys.message.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zs.sys.user.domain.vo.SysUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author zs
 * @since 2025-11-17 09:01:44
 */
@Getter
@Setter
@Schema(description = "消息VO对象")
public class SysMessagesVO implements Serializable {

    @Schema(description = "消息表Id")
    private Long sysMessageId;

    @Schema(description = "消息类型 1-站内信(私信) 2-通知 3-待办")
    private Integer type;

    @Schema(description = "发送者ID")
    private Long senderId;

    @Schema(description = "发送者名称")
    private String senderName;

    @Schema(description = "发送者信息")
    private SysUserVO senderUser;

    @Schema(description = "接收者ID")
    private Long receiverId;

    @Schema(description = "接收者名称")
    private String receiverName;

    @Schema(description = "主题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "是否已读（0: 未读, 1: 已读）")
    private Integer isRead;

    @Schema(description = "阅读时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;



}
