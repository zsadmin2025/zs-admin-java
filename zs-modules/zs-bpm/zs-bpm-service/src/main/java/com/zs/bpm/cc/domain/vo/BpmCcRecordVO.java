package com.zs.bpm.cc.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "抄送记录VO")
public class BpmCcRecordVO implements Serializable {
    @Schema(description = "记录ID")
    private Long id;
    @Schema(description = "流程实例ID")
    private String processInstanceId;
    @Schema(description = "任务ID")
    private String taskId;
    @Schema(description = "抄送人ID")
    private Long userId;
    @Schema(description = "抄送标题")
    private String title;
    @Schema(description = "是否已读")
    private Integer isRead;
    @Schema(description = "阅读时间")
    private String readTime;
    @Schema(description = "抄送时间")
    private String createTime;

    // ==================== 流程实例信息 ====================
    @Schema(description = "流程定义名称（如：请假申请单）")
    private String processDefinitionName;
    @Schema(description = "流程定义Key")
    private String processDefinitionKey;
    @Schema(description = "业务Key")
    private String businessKey;
    @Schema(description = "流程实例名称/单据标题")
    private String processInstanceName;
    @Schema(description = "发起人ID")
    private Long startUserId;
    @Schema(description = "发起人名称")
    private String startUserName;
    @Schema(description = "流程启动时间")
    private Date startTime;
    @Schema(description = "流程结束时间（若未结束则为null）")
    private Date endTime;
    @Schema(description = "流程状态：RUNNING(运行中)/COMPLETED(已结束)/CANCELED(已作废)")
    private String processState;

    // ==================== 抄送发起人信息 ====================
    @Schema(description = "抄送发起人ID（谁抄送给我的）")
    private Long ccSenderId;
    @Schema(description = "抄送发起人名称")
    private String ccSenderName;

    @Schema(description = "抄送类型：1=流程自动(BPMN)，2=手动(API)")
    private Integer ccType;
}
