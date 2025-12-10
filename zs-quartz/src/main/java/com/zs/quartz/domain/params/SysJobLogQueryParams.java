package com.zs.quartz.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务日志查询参数
 */
@Schema(description = "定时任务日志查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysJobLogQueryParams extends BasePageParams {

    @Schema(description = "任务ID")
    private Long sysJobId;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务组名")
    private String jobGroup;

    @Schema(description = "任务状态")
    private Integer status;

}
