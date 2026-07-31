package com.zs.infra.quartz.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 定时任务新增参数
 */
@Schema(description = "定时任务新增参数")
@Data
public class SysJobAddParams {

    @Schema(description = "任务类")
    private String jobClass;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务组")
    private String jobGroup;

    @Schema(description = "任务执行表达式")
    private String cronExpression;

    @Schema(description = "任务状态")
    private Integer status;

    @Schema(description = "任务描述")
    private String remark;
}
