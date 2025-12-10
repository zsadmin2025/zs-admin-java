package com.zs.sys.message.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("sys_messages")
@Schema(description = "消息Entity对象")
public class SysMessagesEntity extends BaseEntity {

    /**  消息表Id */
    @TableId
    private Long sysMessageId;

    /**  消息类型 1-站内信(私信) 2-通知 3-待办 */
    private Integer type;

    /**  发送者ID */
    private Long senderId;

    /**  发送者名称 */
    private String senderName;

    /**  接收者ID */
    private Long receiverId;

    /**  接收者名称 */
    private String receiverName;

    /**  主题 */
    private String title;

    /**  内容 */
    private String content;

    /**  是否已读（0: 未读, 1: 已读） */
    private Integer isRead;

    /**  阅读时间 */
    private Date readTime;





}
