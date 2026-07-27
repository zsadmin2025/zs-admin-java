package com.zs.bpm.model.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
/**
 * 扁平流程结果
 */
@Data
@AllArgsConstructor
public class FlatFlowResult {

    /**
     * 流程定义名称
     */
    private String processName;

    /**
     * 节点列表
     */
    private List<FlowNodeDTO> nodes;

    /**
     * 边列表
     */
    private List<FlowEdgeDTO> edges;

    /**
     * 根据节点ID获取节点信息
     * @param nodeId 节点ID
     * @return 节点信息
     */
    public FlowNodeDTO getNodeById(String nodeId) {
        return nodes.stream()
                .filter(n -> n.getId().equals(nodeId))
                .findFirst()
                .orElse(null);
    }
}
