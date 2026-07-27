package com.zs.bpm.model.domain.dto;

import lombok.Data;

import java.util.List;
/**
 * 流程根节点
 */
@Data
public class FlowRootDTO {

    // 权限
    private List<Object> flowPermission;

    // 节点配置
    private NodeConfigDTO nodeConfig;

    // 流程定义
    private WorkFlowDefDTO workFlowDef;
}
