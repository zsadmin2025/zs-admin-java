package com.zs.sys.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author zsadmin
 */
@Data
@TableName("sys_menu")
@EqualsAndHashCode(callSuper = true)
public class SysMenuEntity extends BaseEntity {

    @TableId
    private Long sysMenuId;

    /**
     * 父级id
     */
    private Long pid;

    /** 路由路径 **/
    private String path;

    /** 路由名称 **/
    private String name;

    /** 菜单类型 **/
    private Integer type;

    /** 组件路径 **/
    private String component;

    /** 菜单标题 **/
    private String title;

    /** 图标 **/
    private String icon;


    /** 链接 **/
    private String link;

    /** 菜单显示状态，0-隐藏 1-显示 **/
    private Integer visible;

    /** 排序 **/
    private Integer sort;

    /** 权限标识 **/
    private String permissions;

    /** 状态 0-禁用 1-启用 **/
    private Integer status;

    private Integer requiresAuth;

    private Integer hideInMenu;

    private Integer hideChildrenInMenu;

    private String activeMenu;

    private Integer noAffix;

    private Integer ignoreCache;
}
