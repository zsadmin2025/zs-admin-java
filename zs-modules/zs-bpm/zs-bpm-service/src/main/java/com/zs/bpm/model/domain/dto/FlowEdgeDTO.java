package com.zs.bpm.model.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * 流程连线DTO
 */
@Data
@AllArgsConstructor
public class FlowEdgeDTO {

    /*
     * 连线ID
     */
    private String id;

    /*
     * 连线源节点ID
     */
    private String sourceId;

    /*
     * 连线目标节点ID
     */
    private String targetId;

    /*
     * 连线条件配置
     */
    private ConditionConfigDTO conditionConfig;

}
