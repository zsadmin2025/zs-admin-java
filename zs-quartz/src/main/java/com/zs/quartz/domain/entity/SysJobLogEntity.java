package com.zs.quartz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务日志
 */
@TableName("sys_job_log")
@Data
public class SysJobLogEntity {

    @TableId
    private Long sysJobLogId;
    private Long sysJobId;
    private String jobClass;
    private String jobName;
    private String jobGroup;
    private String jobMessage;
    private int status;
    private String exceptionInfo;
    private Date startTime;
    private Date endTime;
    private Long duration;


}
