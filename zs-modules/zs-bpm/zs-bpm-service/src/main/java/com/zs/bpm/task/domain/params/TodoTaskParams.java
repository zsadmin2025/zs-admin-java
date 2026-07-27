package com.zs.bpm.task.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoTaskParams {

    @NotNull(message = "流程实例ID不能为空")
    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "任务ID（可选，用于从待办/已办进入时定位具体任务）")
    private String taskId;
}
