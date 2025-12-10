package com.zs.sys.menu.controller;


import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import com.zs.sys.menu.domain.params.*;
import com.zs.sys.menu.domain.vo.SysMenuBtnPermissionsVO;
import com.zs.sys.menu.domain.vo.SysMenuListVO;
import com.zs.sys.menu.domain.vo.SysMenuVO;
import com.zs.sys.menu.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/menu")
@Tag(name = "菜单管理", description = "菜单管理")
public class SysMenuController {


    @Resource
    private ISysMenuService iSysMenuService;

    @Operation(summary = "分页获取菜单信息")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:menu:page')")
    public Result<PageResult<SysMenuVO>> page(SysMenuQueryParams sysMenuQueryParams) {
        PageResult<SysMenuVO> iPage = iSysMenuService.page(sysMenuQueryParams);
        return new Result<PageResult<SysMenuVO>>().ok(iPage);
    }

    @Operation(summary = "获取菜单导航")
    @GetMapping("nav")
    public Result<List<SysMenuVO>> nav() {
        List<SysMenuVO> list = iSysMenuService.getNavList();
        return new Result<List<SysMenuVO>>().ok(list);
    }

    @Operation(summary = "获取菜单列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<List<SysMenuListVO>> list(SysMenuQueryParams sysMenuQueryParams) {
        List<SysMenuListVO> list = iSysMenuService.getList(sysMenuQueryParams);
        return new Result<List<SysMenuListVO>>().ok(list);
    }

    @Operation(summary = "获取菜单列表(携带子级按钮权限)")
    @GetMapping("listPermission")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result<List<SysMenuListVO>> listPermission(SysMenuQueryParams sysMenuQueryParams) {
        List<SysMenuListVO> list = iSysMenuService.listPermission(sysMenuQueryParams);
        return new Result<List<SysMenuListVO>>().ok(list);
    }

    @Operation(summary = "新增菜单信息")
    @Log(module = "菜单管理-新增", type = OperationTypeEnum.ADD, description = "新增菜单信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result<?> save(@Valid @RequestBody SysMenuAddParams sysMenuAddParams) {

        iSysMenuService.save(sysMenuAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改菜单信息")
    @Log(module = "菜单管理-修改", type = OperationTypeEnum.UPDATE, description = "新增菜单信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result<?> update(@RequestBody SysMenuUpdateParams sysMenuUpdateParams) {
        iSysMenuService.update(sysMenuUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "获取菜单信息")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:menu:info')")
    public Result<SysMenuListVO> get(@PathVariable("id") Long id) {
        SysMenuListVO sysMenuVO = iSysMenuService.getById(id);
        return new Result<SysMenuListVO>().ok(sysMenuVO);
    }

    @Operation(summary = "删除菜单信息")
    @Log(module = "菜单管理-删除", type = OperationTypeEnum.DELETE, description = "删除菜单信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        iSysMenuService.delete(id);
        return new Result<>().ok();
    }


    @Operation(summary = "根据菜单ID获取按钮权限")
    @GetMapping("{id}/permissions/list")
    @PreAuthorize("hasAuthority('sys:menu:btnPermissions:list')")
    public Result<List<SysMenuBtnPermissionsVO>> getPermissions(@PathVariable("id") Long id) {
        List<SysMenuBtnPermissionsVO> permissions = iSysMenuService.getPermissionsBySysMenuId(id);
        return new Result<List<SysMenuBtnPermissionsVO>>().ok(permissions);
    }

    @Operation(summary = "新增菜单权限按钮")
    @Log(module = "菜单管理-新增", type = OperationTypeEnum.ADD, description = "菜单权限按钮")
    @PostMapping("permissions/save")
    @PreAuthorize("hasAuthority('sys:menu:btnPermissions:save')")
    public Result<?> saveBtnPermissions(@RequestBody SysMenuBtnPermissionsAddParams sysMenuBtnPermissionsAddParams) {
        iSysMenuService.saveBtnPermissions(sysMenuBtnPermissionsAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改菜单权限按钮")
    @Log(module = "菜单管理-修改", type = OperationTypeEnum.UPDATE, description = "修改菜单权限按钮")
    @PutMapping("permissions/update")
    @PreAuthorize("hasAuthority('sys:menu:btnPermissions:update')")
    public Result<?> updateBtnPermissions(@RequestBody SysMenuBtnPermissionsUpdateParams sysMenuBtnPermissionsUpdateParams) {
        iSysMenuService.updateBtnPermissions(sysMenuBtnPermissionsUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "根据菜单ID获取按钮权限")
    @GetMapping("permissions/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:btnPermissions:info')")
    public Result<SysMenuBtnPermissionsVO> getPermissionsBySysMenuId(@PathVariable("id") Long id) {
        SysMenuBtnPermissionsVO permissions = iSysMenuService.getBtnPermissionsBySysMenuId(id);
        return new Result<SysMenuBtnPermissionsVO>().ok(permissions);
    }

    @Operation(summary = "删除菜单权限按钮")
    @Log(module = "菜单管理-删除", type = OperationTypeEnum.DELETE, description = "删除菜单权限按钮")
    @DeleteMapping("permissions/{id}")
    @PreAuthorize("hasAuthority('sys:menu:btnPermissions:delete')")
    public Result<?> deleteBtnPermissions(@PathVariable("id") Long id) {
        iSysMenuService.deleteBtnPermissions(id);
        return new Result<>().ok();
    }
}
