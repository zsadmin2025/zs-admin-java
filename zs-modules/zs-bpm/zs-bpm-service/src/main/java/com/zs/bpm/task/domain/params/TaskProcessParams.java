package com.zs.bpm.task.domain.params;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 流程任务参数
 *
 * @author zs
 */
@Data
public class TaskProcessParams {


    /**
     * 流程实例ID
     */
    @NotNull(message = "流程实例ID不能为空")
    private String processDefinitionId;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;
}
