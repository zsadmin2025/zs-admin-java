package com.zs.sys.role.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity {

    @TableId
    private Long sysRoleMenuId;

    private Long sysRoleId;

    private Long sysMenuId;
}
