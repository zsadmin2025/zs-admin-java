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
public class SysMenuListVO extends TreeNode<SysMenuListVO> implements Serializable {


    @Schema(description = "菜单id")
    private Long sysMenuId;

    @Schema(description = "父级id")
    private Long pid;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "路由名称")
    private String name;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "菜单类型")
    private Integer type;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单显示状态，0-隐藏 1-显示")
    private Integer visible;



    @Schema(description = "权限标识")
    private String permissions;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "菜单状态，0-禁用 1-启用")
    private Integer status;

    @Schema(description = "当前页面是否需要登录才能访问")
    private boolean requiresAuth;



    @Schema(description = "是否隐藏菜单,设为 true 时，不在侧边菜单中显示")
    private boolean hideInMenu;

    @Schema(description = "是否隐藏子菜单,设为 true 时，子菜单不在侧边栏显示")
    private boolean hideChildrenInMenu;

    @Schema(description = "当前激活的菜单, 设置该属性后，菜单将按指定名称高亮显示")
    private String activeMenu;



    @Schema(description = "是否固定在标签页中,设为 true 时，标签页不会固定在标签栏")
    private boolean noAffix;

    @Schema(description = "设为 true 时，页面不会被缓存")
    private boolean ignoreCache;


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
