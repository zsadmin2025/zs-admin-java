package com.zs.bpm.task.domain.vo;

import com.zs.bpm.process.domain.vo.AssigneeUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 节点审批人记录
 * <p>
 * 一个审批节点（会签/或签）可能产生多个任务实例，每个任务实例对应一个审批人记录，
 * 包含该审批人的处理人信息、审批意见、时间及状态。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Data
@Schema(description = "节点审批人记录")
public class NodeApprover {

    @Schema(description = "任务实例ID")
    private String taskId;

    @Schema(description = "审批人信息")
    private AssigneeUserVO assigneeUser;

    @Schema(description = "原始处理人（转办/委派前的处理人）")
    private AssigneeUserVO originalAssigneeUser;

    @Schema(description = "审批意见")
    private String comment;

    @Schema(description = "审批开始时间")
    private Date startTime;

    @Schema(description = "审批完成时间")
    private Date endTime;

    @Schema(description = "耗时（毫秒）")
    private Long durationInMillis;

    @Schema(description = "审批人状态：COMPLETED-已审批 / IN_PROGRESS-待审批")
    private String status;

}
