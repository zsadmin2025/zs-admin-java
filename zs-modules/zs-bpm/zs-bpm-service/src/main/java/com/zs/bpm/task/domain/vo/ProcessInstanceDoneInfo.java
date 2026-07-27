package com.zs.bpm.task.domain.vo;

import com.zs.bpm.process.domain.vo.AssigneeUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "流程实例基本信息")
public class ProcessInstanceDoneInfo {

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义名称（如：请假申请单）")
    private String processDefinitionName;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程实例名称/单据标题（如：管理员发起的请假申请单）")
    private String processInstanceName;

    @Schema(description = "业务表单单号/业务主键Key")
    private String businessKey;

    @Schema(description = "发起人用户信息")
    private AssigneeUserVO startUser;

    @Schema(description = "流程启动时间")
    private Date startTime;

    @Schema(description = "流程结束时间（若未结束则为 null）")
    private Date endTime;

    @Schema(description = "流程状态：RUNNING(运行中)/COMPLETED(已结束)/CANCELED(已作废)")
    private String processState;

    @Schema(description = "总耗时（毫秒数，已结束单据适用）")
    private Long durationInMillis;

    @Schema(description = "任务信息（用户正在处理的任务，含任务ID、名称、办理人、到期时间等）")
    private FlowNode doneTask;

    @Schema(description = "模型JSON")
    private String modelJson;


}
