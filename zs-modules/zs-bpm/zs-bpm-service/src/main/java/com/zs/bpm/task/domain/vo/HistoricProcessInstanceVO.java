package com.zs.bpm.task.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.engine.history.HistoricProcessInstance;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class HistoricProcessInstanceVO implements Serializable {

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "业务键")
    private String businessKey;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程定义名称")
    private String processDefinitionName;

    @Schema(description = "当前使用的流程模板版本号")
    private int processDefinitionVersion;

    @Schema(description = "流程实例名称", example = "张三的请假流程")
    private String name;

    @Schema(description = "流程实例开始时间")
    private Date startTime;

    @Schema(description = "流程实例结束时间")
    private Date endTime;

    @Schema(description = "流程实例持续时间毫秒")
    private Long durationInMillis;

    @Schema(description = "流程实例开始用户ID")
    private String startUserId;

    @Schema(description = "流程实例开始活动ID")
    private String startActivityId;

    @Schema(description = "流程实例结束活动ID")
    private String endActivityId;

    // 类型转换
    public static HistoricProcessInstanceVO convert(HistoricProcessInstance instance) {
        HistoricProcessInstanceVO vo = new HistoricProcessInstanceVO();
        vo.setProcessInstanceId(instance.getId());
        vo.setBusinessKey(instance.getBusinessKey());
        vo.setProcessDefinitionId(instance.getProcessDefinitionId());
        vo.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        vo.setProcessDefinitionName(instance.getProcessDefinitionName());
        vo.setProcessDefinitionVersion(instance.getProcessDefinitionVersion());
        vo.setName(instance.getName());
        vo.setStartTime(instance.getStartTime());
        vo.setEndTime(instance.getEndTime());
        vo.setDurationInMillis(instance.getDurationInMillis());
        vo.setStartUserId(instance.getStartUserId());
        vo.setStartActivityId(instance.getStartActivityId());
        vo.setEndActivityId(instance.getEndActivityId());
        return vo;
        }

    // List<T>类型转换
    public static List<HistoricProcessInstanceVO> convert(List<HistoricProcessInstance> instances) {
        return instances.stream().map(HistoricProcessInstanceVO::convert).toList();
    }


}