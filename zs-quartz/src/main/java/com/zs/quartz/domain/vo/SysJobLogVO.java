package com.zs.quartz.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 定时任务日志
 */
@Schema(description = "定时任务日志VO对象")
@Data
public class SysJobLogVO {

    @Schema(description = "定时任务日志ID")
    private Long sysJobLogId;

    @Schema(description = "定时任务ID")
    private Long sysJobId;

    @Schema(description = "定时任务类名")
    private String jobClass;

    @Schema(description = "定时任务名称")
    private String jobName;

    @Schema(description = "定时任务组名")
    private String jobGroup;

    @Schema(description = "定时任务消息")
    private String jobMessage;

    @Schema(description = "定时任务状态")
    private int status;

    @Schema(description = "定时任务异常信息")
    private String exceptionInfo;

    @Schema(description = "定时任务开始时间")
    private Date startTime;

    @Schema(description = "定时任务结束时间")
    private Date endTime;

    @Schema(description = "定时任务耗时")
    private Long duration;


}
