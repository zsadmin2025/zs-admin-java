package com.zs.sys.menu.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 菜单权限按钮新增参数
 */
@Data
@Schema(description = "菜单权限按钮新增参数")
public class SysMenuBtnPermissionsAddParams {

    @Schema(description = "上级菜单id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pid;

    @Schema(description = "按钮名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "按钮类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Integer type;

    @Schema(description = "权限标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys:menu:save")
    private String permissions;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "按钮状态，0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
}
