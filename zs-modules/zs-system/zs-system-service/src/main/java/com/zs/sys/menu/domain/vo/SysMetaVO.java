package com.zs.sys.menu.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
public class SysMetaVO {


    @Schema(description = "当前页面是否需要登录才能访问")
    private boolean requiresAuth;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单标题")
    private String title;

    @Schema(description = "是否隐藏菜单,设为 true 时，不在侧边菜单中显示")
    private boolean hideInMenu;

    @Schema(description = "是否隐藏子菜单,设为 true 时，子菜单不在侧边栏显示")
    private boolean hideChildrenInMenu;

    @Schema(description = "当前激活的菜单, 设置该属性后，菜单将按指定名称高亮显示")
    private String activeMenu;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否固定在标签页中,设为 true 时，标签页不会固定在标签栏")
    private boolean noAffix;

    @Schema(description = "设为 true 时，页面不会被缓存")
    private boolean ignoreCache;
}
