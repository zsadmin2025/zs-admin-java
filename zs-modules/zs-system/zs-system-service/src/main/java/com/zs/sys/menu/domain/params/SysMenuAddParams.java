package com.zs.sys.menu.domain.params;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zsadmin
 */
@Data
@Schema(description = "菜单新增参数")
public class SysMenuAddParams {

    @Schema(description = "父级id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "父级id不能为空")
    private Long pid;

    @Schema(description = "路由地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "路由地址不能为空")
    private String path;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @Schema(description = "菜单类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    @Schema(description = "组件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "组件路径不能为空")
    private String component;

    @Schema(description = "菜单标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "菜单标题不能为空")
    private String title;

    @Schema(description = "菜单图标", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "菜单图标不能为空")
    private String icon;

    @Schema(description = "跳转链接")
    private String link;

    @Schema(description = "菜单显示状态，0-隐藏 1-显示", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer visible;

    private Integer sort;

    @Schema(description = "权限标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "sys:menu:save")
    private String permissions;

    @Schema(description = "菜单状态，0-禁用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED)
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
}
