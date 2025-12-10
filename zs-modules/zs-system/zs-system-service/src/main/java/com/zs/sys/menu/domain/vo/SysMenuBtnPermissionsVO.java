package com.zs.sys.menu.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 按钮权限
 */
@Data
public class SysMenuBtnPermissionsVO implements Serializable {

    @Schema(description = "按钮id")
    private Long sysMenuId;
    @Schema(description = "按钮名称")
    private String title;
    @Schema(description = "权限标识")
    private String permissions;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "按钮状态，0-禁用 1-启用")
    private Integer status;
}
