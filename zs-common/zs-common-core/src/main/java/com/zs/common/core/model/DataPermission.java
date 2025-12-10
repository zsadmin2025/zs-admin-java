package com.zs.common.core.model;

import com.zs.common.core.enums.DataScopeEnum;
import lombok.Data;

import java.util.Set;

@Data
public class DataPermission {

    private Long userId;

    private Long deptId;

    private Set<Long> deptIds;

    private Set<Long> roleIds;

    private Set<String> permissions;

    /**
     * 数据权限类型
     */
    private Set<DataScopeEnum> dataScopeTypes;
}
