package com.zs.bpm.task.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 任务审批参数
 */
@Data
public class TaskApprovalParams {

    @Schema(description = "是否通过")
    private boolean approved;

    @Schema(description = "审批状态")
    private String approvalStatus;

    @Schema(description = "审批意见")
    private String approvalOpinion;

}
