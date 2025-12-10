package com.zs.sys.message.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@Schema(description = "消息electQueryParams对象")
public class SysMessagesSelectQueryParams implements Serializable {

    @Schema(description = "消息表Id")
    private Long sysMessageId;

    @Schema(description = "消息类型 1-站内信(私信) 2-通知 3-待办")
    private Integer type;

    @Schema(description = "发送者ID")
    private Long senderId;

    @Schema(description = "接收者ID")
    private Long receiverId;

    @Schema(description = "主题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "是否已读（0: 未读, 1: 已读）")
    private Integer isRead;


}
