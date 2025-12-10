package com.zs.sys.notice.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@Data
@Schema(description = "通知公告")
public class SysNoticeEntity extends BaseEntity {



    /**
     * 通知公告id
     */
    @TableId
    private Long sysNoticeId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 通知公告类型: 通知、公告、其他
     */
    private Integer type;
    /**
     * 通知公告等级: 普通、一般、紧急
     */
    private Integer level;
    /**
     * 状态:0撤销，1草稿，2已发布
     */
    private Integer status;
    /**
     * 发布时间
     */
    private String releaseTime;

    @TableField(exist = false)
    private String realName;

}
