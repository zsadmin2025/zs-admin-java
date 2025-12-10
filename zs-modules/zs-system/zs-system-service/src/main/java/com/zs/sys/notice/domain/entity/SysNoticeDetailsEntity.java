package com.zs.sys.notice.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@TableName("sys_notice_details")
@Schema(description = "通知公告详情")
public class SysNoticeDetailsEntity{


    /** 通知公告详情id */
    @TableId
    private Long sysNoticeDetailsId;
    /** 通知公告id */
    private Long sysNoticeId;
    /** 用户id */
    private Long receiverId;
    /** 用户名 */
    @TableField(exist = false)
    private String realName;
    /** 状态:1已读，2未读 */
    private Integer status;
    /** 阅读时间 */
    private String readTime;
}

