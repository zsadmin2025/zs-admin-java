package com.zs.bpm.process.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "审批轨迹VO")
public class ApprovalTraceVO implements Serializable {
    @Schema(description = "节点ID")
    private String activityId;
    @Schema(description = "节点名称")
    private String activityName;
    @Schema(description = "处理人ID")
    private String assignee;
    @Schema(description = "处理人名称")
    private String assigneeName;
    @Schema(description = "开始时间")
    private String startTime;
    @Schema(description = "结束时间")
    private String endTime;
    @Schema(description = "审批结果")
    private String result;
    @Schema(description = "审批意见")
    private String comment;
    @Schema(description = "耗时(毫秒)")
    private Long durationInMillis;
    @Schema(description = "子节点(并行分支时)")
    private List<ApprovalTraceVO> children;
}
