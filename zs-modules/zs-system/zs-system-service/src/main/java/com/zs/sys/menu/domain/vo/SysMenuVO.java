package com.zs.sys.menu.domain.vo;


import com.zs.common.core.utils.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author zsadmin
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "菜单VO")
public class SysMenuVO extends TreeNode<SysMenuVO> implements Serializable {


    @Schema(description = "菜单id")
    private Long sysMenuId;

    @Schema(description = "父级id")
    private Long pid;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "菜单类型")
    private Integer type;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单显示状态，0-隐藏 1-显示")
    private Integer visible;



    @Schema(description = "权限标识")
    private String permissions;

    @Schema(description = "菜单状态，0-禁用 1-启用")
    private Integer status;

    private SysMetaVO meta;


    @SuppressWarnings("unused")
    public Long getSysMenuId() {
        return sysMenuId;
    }

    @SuppressWarnings("unused")
    public void setSysMenuId(Long sysMenuId) {
        this.sysMenuId = sysMenuId;
        this.setId(sysMenuId);
    }
}
