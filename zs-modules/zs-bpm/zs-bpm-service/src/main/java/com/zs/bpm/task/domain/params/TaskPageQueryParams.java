package com.zs.bpm.task.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "任务查询参数")
public class TaskPageQueryParams extends BasePageParams {

    @Schema(description = "任务名称(模糊搜索)")
    private String taskName;

    @Schema(description = "流程名称(模糊搜索)")
    private String processDefinitionName;

    @Schema(description = "流程实例名称(模糊搜索)")
    private String processInstanceName;

    @Schema(description = "流程单据")
    private String businessKey;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

}
