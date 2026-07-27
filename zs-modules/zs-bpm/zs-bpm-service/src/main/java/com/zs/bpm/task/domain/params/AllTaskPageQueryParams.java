package com.zs.bpm.task.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程任务菜单 —— 全部任务查询参数
 *
 * @author zsadmin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "全部任务查询参数")
public class AllTaskPageQueryParams extends BasePageParams {

    @Schema(description = "流程单据名称（模糊搜索）")
    private String processDefinitionName;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "业务单号（模糊搜索）")
    private String businessKey;

    @Schema(description = "任务节点名称（模糊搜索）")
    private String taskName;

    @Schema(description = "审批状态：RUNNING-审批中 / COMPLETED-已完成")
    private String approvalStatus;

    @Schema(description = "审批人用户ID")
    private Long assigneeId;
}
