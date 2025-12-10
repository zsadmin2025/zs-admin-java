package com.zs.sys.role.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zsadmin
 */
@Schema(description = "角色菜单关联添加参数")
@Data
public class SysRoleMenuAddParams {

    @Schema(description = "角色ID")
    private Long sysRoleId;

    @Schema(description = "菜单ID")
    private Long sysMenuId;
}
