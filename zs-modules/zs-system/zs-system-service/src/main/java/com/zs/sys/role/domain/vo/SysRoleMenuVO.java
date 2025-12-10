package com.zs.sys.role.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@Schema(description = "角色菜单关联表")
@Data
public class SysRoleMenuVO implements Serializable {

    @Schema(description = "角色菜单关联表ID")
    private Long sysRoleMenuId;

    @Schema(description = "角色ID")
    private Long sysRoleId;

    @Schema(description = "菜单ID")
    private Long sysMenuId;
}
