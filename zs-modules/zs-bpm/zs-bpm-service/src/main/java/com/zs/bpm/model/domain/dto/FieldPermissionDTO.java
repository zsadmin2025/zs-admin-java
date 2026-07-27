package com.zs.bpm.model.domain.dto;

import lombok.Data;
/**
 * 表单字段权限
 */
@Data
public class FieldPermissionDTO {

    private String field;
    private String title;
    private String permission;     // 1只读 2必填可编辑 3隐藏
}
