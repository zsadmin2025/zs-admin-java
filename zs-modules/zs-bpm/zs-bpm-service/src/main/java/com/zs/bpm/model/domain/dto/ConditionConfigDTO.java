package com.zs.bpm.model.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 条件配置
 */
@Data
public class ConditionConfigDTO {

    private String logic;          // AND/OR
    private List<ConditionItemDTO> conditions; // 条件项
    private Boolean isOther;       // 是否默认分支
    private String expression;     // 原始EL表达式，如 "${days > 3}" 或 "days > 3"
}
