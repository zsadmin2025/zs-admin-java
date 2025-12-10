package com.zs.quartz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务表
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_job")
@Data
public class SysJobEntity extends BaseEntity {

    @TableId
    private Long sysJobId;

    private String jobClass;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * cron执行表达式
     */
    private String cronExpression;
//    /**
//     * cron计划策略
//     */
//    private String misfirePolicy = "1";
//    /**
//     * 是否并发执行（0允许 1禁止）
//     */
//    private String concurrent;
    /**
     * 任务状态（0暂停 1正常）
     */
    private Integer status;

    private String remark;
}
