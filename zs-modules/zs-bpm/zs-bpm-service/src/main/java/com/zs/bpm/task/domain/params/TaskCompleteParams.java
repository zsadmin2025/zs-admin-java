package com.zs.bpm.task.domain.params;

import com.zs.common.core.enums.BpmTaskActionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(description = "任务完成参数")
public class TaskCompleteParams {

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "审批动作: APPROVE=通过, REJECT=驳回, TRANSFER=转办, DELEGATE=委派")
    private BpmTaskActionEnum action;

    @Schema(description = "审批意见")
    private String comment;

    @Schema(description = "表单数据")
    private Map<String, Object> formData;

    @Schema(description = "流程变量")
    private TaskApprovalParams variables;

    @Schema(description = "驳回目标: INITIATOR=发起人, PREV=上一节点, ANY=指定节点")
    private String rejectTarget;

    @Schema(description = "驳回目标节点ID(当rejectTarget=ANY时)")
    private String rejectTargetActivityId;

    @Schema(description = "转办/委派目标人ID")
    private Long targetUserId;

    @Schema(description = "加签用户ID列表")
    private List<Long> signUserIds;

    @Schema(description = "加签位置: BEFORE=前加签, AFTER=后加签")
    private String signPosition;

    @Schema(description = "抄送人ID列表")
    private List<Long> ccUserIds;
}
