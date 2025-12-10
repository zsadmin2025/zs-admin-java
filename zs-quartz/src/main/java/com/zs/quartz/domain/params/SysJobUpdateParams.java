package com.zs.quartz.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 定时任务新增参数
 */
@Schema(description = "定时任务更新参数")
@Data
public class SysJobUpdateParams {

    @Schema(description = "定时任务ID")
    private Long sysJobId;

    @Schema(description = "定时任务名称")
    private String jobName;

    @Schema(description = "定时任务分组")
    private String jobGroup;

    @Schema(description = "定时任务表达式")
    private String cronExpression;

    @Schema(description = "定时任务状态")
    private Integer status;

    @Schema(description = "定时任务描述")
    private String remark;
}
