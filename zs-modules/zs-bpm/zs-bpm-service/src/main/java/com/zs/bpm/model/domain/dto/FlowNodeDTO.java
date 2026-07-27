package com.zs.bpm.model.domain.dto;

import lombok.Data;
/**
 * 流程节点(扁平化节点)
 */
@Data
public class FlowNodeDTO {

    private String id;
    private String type;           // startEvent/endEvent/userTask/serviceTask/exclusiveGateway
    private String name;
    private NodeConfigDTO rawConfig;
    private String defaultFlowId;

    public FlowNodeDTO(String id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public FlowNodeDTO(String id, String type, String name, NodeConfigDTO rawConfig) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.rawConfig = rawConfig;
    }
}
