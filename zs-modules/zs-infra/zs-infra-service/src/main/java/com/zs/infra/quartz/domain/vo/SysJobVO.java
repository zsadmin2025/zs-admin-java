package com.zs.infra.quartz.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 定时任务视图对象
 */
@Schema(description = "定时任务VO对象")
@Data
public class SysJobVO {


    @Schema(description = "定时任务ID")
    private Long sysJobId;

    @Schema(description = "定时任务类")
    private String jobClass;

    @Schema(description = "定时任务名称")
    private String jobName;

    @Schema(description = "定时任务组")
    private String jobGroup;

    @Schema(description = "定时任务表达式")
    private String cronExpression;

    @Schema(description = "定时任务状态")
    private Integer status;

    @Schema(description = "定时任务描述")
    private String remark;

    @Schema(description = "定时任务创建时间")
    private String createTime;
}
