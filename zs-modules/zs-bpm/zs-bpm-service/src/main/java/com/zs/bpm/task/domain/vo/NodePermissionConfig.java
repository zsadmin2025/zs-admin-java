package com.zs.bpm.task.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 节点权限配置实体
 * 封装一个节点的所有权限信息
 */
@Data
public class NodePermissionConfig {

    /**
     * 当前节点的按钮权限列表
     */
    private List<ButtonPermission> buttonPermissions = List.of();

    /**
     * 当前节点的字段权限列表
     */
    private List<FieldPermission> fieldPermissions = List.of();
}
