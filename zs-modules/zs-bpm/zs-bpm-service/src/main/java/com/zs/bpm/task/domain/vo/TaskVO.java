package com.zs.bpm.task.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.identitylink.api.IdentityLink;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 流程任务前端展示VO
 * 适用场景：待办、已办、全部任务列表、任务详情查询
 */
@Data
@Schema(description = "流程任务前端展示VO")
public class TaskVO {

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程定义名称")
    private String processDefinitionName;

    @Schema(description = "流程实例名称/单据标题")
    private String processInstanceName;

    @Schema(description = "任务业务编号")
    private String businessKey;

    @Schema(description = "发起人用户ID")
    private String startUserId;

    @Schema(description = "发起人用户名")
    private String startUserName;

    @Schema(description = "发起人部门ID")
    private String startDeptId;

    @Schema(description = "发起人部门名称")
    private String startDeptName;


    @Schema(description = "流程启动时间")
    private Date startTime;

    @Schema(description = "流程结束时间（未结束为null）")
    private Date endTime;




    @Schema(description = "任务ID")
    private  String id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "任务节点描述")
    private String description;

    /**
     * 转办 / 委托代办时存储原始处理人；普通待办为空，仅后台用于流转记录溯源
     */
    @Schema(description = "任务所有者")
    private String owner;


    /**
     * 任务每转办移交一次数值 + 1，统计任务流转次数，追溯移交历史
     */
    @Schema(description = "经办人变更次数")
    private Integer assigneeUpdatedCount;


    @Schema(description = "原始处理人")
    private String originalAssignee;

    @Schema(description = "当前处理人")
    private String assignee;

    /**
     * 枚举：PENDING (待代办)、RESOLVED (代办完成归还)；无委托场景为空
     */
    @Schema(description = "任务委托状态")
    private String delegationState;

    /**
     * 子流程、多实例会签 / 或签任务才有值，区分主、子任务，普通审批为空
     */
    @Schema(description = "父任务ID")
    private String parentTaskId;

    @Schema(description = "任务本地化名称")
    private String localizedName;

    @Schema(description = "任务本地化描述")
    private String localizedDescription;

    @Schema(description = "任务优先级")
    private Integer priority;

    /**
     * created = 新建候选待认领；claimed = 已分配经办人；inProgress = 处理中；suspended = 挂起冻结；completed = 完成；terminated = 作废终止
     */
    @Schema(description = "任务状态")
    private String state;

    @Schema(description = "任务创建时间")
    private Date createTime;

    @Schema(description = "任务处理中开始时间")
    private Date inProgressStartTime;

    @Schema(description = "处理状态启动人")
    private String inProgressStartedBy;

    @Schema(description = "任务认领时间")
    private Date inProgressCompleteTime;

    @Schema(description = "任务认领人")
    private String claimedBy;

    @Schema(description = "任务挂起时间")
    private Date suspendedTime;

    @Schema(description = "任务挂起人")
    private String suspendedBy;

    /**
     * 单据审批截止时间，前端表格「待办时效」；超时文字标红，null 代表无期限
     */
    @Schema(description = "任务到期时间")
    private Date dueDate;
    /**
     * 1 = 正常激活；2 = 挂起冻结；taskQuery.active() 底层依靠此字段过滤冻结任务
     */
    @Schema(description = "任务挂起状态")
    private Integer suspensionState;

    @Schema(description = "任务类别")
    private String category;

    /**
     * 存储候选人、抄送知会人、候选用户组数据，查询「抄送我的」任务依赖此集合
     */
    @Schema(description = "任务候选处理人")
    List<IdentityLink> taskIdentityLinkEntities;

    @Schema(description = "节点唯一ID")
    private String taskDefinitionId;

    @Schema(description = "节点唯一编码")
    private String taskDefinitionKey;

    /**
     * true = 任务已撤销作废，false = 正常有效任务，用于过滤作废待办
     */
    @Schema(description = "任务是否取消")
    private Boolean isCanceled;

    @Schema(description = "查询变量")
    private Map<String, Object> queryVariables;

    @Schema(description = "任务版本")
    private Integer revision;


    @Schema(description = "新增持久化标记")
    private Boolean isInserted;

    @Schema(description = "修改持久化标记")
    private Boolean isUpdated;

    @Schema(description = "逻辑删除标记")
    private Boolean isDeleted;
}
