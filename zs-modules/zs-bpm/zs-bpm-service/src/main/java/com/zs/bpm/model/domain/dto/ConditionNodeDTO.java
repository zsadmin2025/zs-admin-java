package com.zs.bpm.model.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 条件分支节点
 */
@Data
public class ConditionNodeDTO {

    private String nodeName;
    private Integer type;
    private Integer priorityLevel;
    private Boolean isOther;       // 是否默认分支
    private String configMode;     // 条件模式: expression/group
    private String expression;     // 原始EL表达式，如 "${days > 3}"
    private List<ConditionItemDTO> conditionList;
    private NodeConfigDTO childNode;
}
