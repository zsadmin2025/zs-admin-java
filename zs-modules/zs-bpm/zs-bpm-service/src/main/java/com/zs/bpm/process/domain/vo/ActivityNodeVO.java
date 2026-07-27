package com.zs.bpm.process.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 流程活动节点 VO（含该节点下的审批任务）
 *
 * @author zsadmin
 */
@Data
@Schema(description = "流程活动节点（含审批任务）")
public class ActivityNodeVO implements Serializable {

    @Schema(description = "节点ID")
    private String id;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "节点类型 1=结束 10=发起人 13=办理人")
    private Integer nodeType;

    @Schema(description = "节点状态 1=待处理 2=已完成")
    private Integer status;

    @Schema(description = "开始时间（毫秒时间戳）")
    private Long startTime;

    @Schema(description = "结束时间（毫秒时间戳）")
    private Long endTime;

    @Schema(description = "该节点下的审批任务列表")
    private List<ActivityNodeTaskVO> tasks;

    @Schema(description = "候选人策略")
    private Integer candidateStrategy;

    @Schema(description = "审批人")
    private AssigneeUserVO assigneeUser;

    @Schema(description = "候选人列表")
    private List<AssigneeUserVO> candidateUsers;

    @Schema(description = "关联流程实例ID")
    private String processInstanceId;
}
