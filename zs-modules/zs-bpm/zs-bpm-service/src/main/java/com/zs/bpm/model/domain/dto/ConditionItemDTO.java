package com.zs.bpm.model.domain.dto;

import lombok.Data;
/**
 * 条件项
 */
@Data
public class ConditionItemDTO {

    private String columnId;       // 表单字段key
    private String showName;       // 字段显示名
    private String columnType;     // Number/String
    private String opt1;           // 运算符 > < == != contains
    private String zdy1;           // 对比值
}
