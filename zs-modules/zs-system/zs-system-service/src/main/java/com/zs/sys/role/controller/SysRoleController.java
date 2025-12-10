package com.zs.sys.role.controller;


import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.role.domain.excel.SysRoleExcel;
import com.zs.sys.role.domain.params.SysRoleAddParams;
import com.zs.sys.role.domain.params.SysRoleQueryParams;
import com.zs.sys.role.domain.vo.SysRoleVO;
import com.zs.sys.role.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/role")
@Tag(name = "角色管理")
public class SysRoleController {

    @Resource
    private ISysRoleService iSysRoleService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:role:page')")
    public Result<PageResult<SysRoleVO>> page(SysRoleQueryParams sysRoleQueryParams) {
        PageResult<SysRoleVO> iPage = iSysRoleService.page(sysRoleQueryParams);
        return new Result<PageResult<SysRoleVO>>().ok(iPage);
    }

    @Operation(summary = "角色列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result<List<SysRoleVO>> list(SysRoleQueryParams sysRoleQueryParams) {
        List<SysRoleVO> list = iSysRoleService.getList(sysRoleQueryParams);
        return new Result<List<SysRoleVO>>().ok(list);
    }

    @Operation(summary = "新增角色信息")
    @Log(module = "角色管理-新增", type = OperationTypeEnum.ADD, description = "新增角色信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result<?> save(@RequestBody SysRoleAddParams sysRoleAddParams) {

        iSysRoleService.save(sysRoleAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改角色信息")
    @Log(module = "角色管理-修改", type = OperationTypeEnum.UPDATE, description = "修改角色信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result<?> update(@RequestBody SysRoleAddParams sysPostAddParams) {
        iSysRoleService.update(sysPostAddParams);
        return new Result<>().ok();
    }


    @Operation(summary = "角色信息详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:role:info')")
    public Result<SysRoleVO> get(@PathVariable("id") Long id) {
        SysRoleVO sysRoleVO = iSysRoleService.getById(id);
        return new Result<SysRoleVO>().ok(sysRoleVO);
    }


    @Operation(summary = "删除角色信息")
    @Log(module = "角色管理-删除", type = OperationTypeEnum.DELETE, description = "删除角色信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        iSysRoleService.deleteById(id);
        return new Result<>().ok();
    }


    @Operation(summary = "批量删除角色信息")
    @Log(module = "角色管理-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除角色信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:role:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        iSysRoleService.batchDelById(Arrays.asList(ids));
        return new Result<>().ok();
    }

    @Operation(summary = "导出角色信息")
    @Log(module = "角色管理-导出", type = OperationTypeEnum.EXPORT, description = "导出角色信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:role:export')")
    public void export(HttpServletResponse response, SysRoleQueryParams sysRoleQueryParams) throws IOException {
        List<SysRoleVO> list = iSysRoleService.getList(sysRoleQueryParams);
        List<SysRoleExcel> excelList = BeanUtil.copyToList(list, SysRoleExcel.class);
        ExcelUtils.exportExcel(response, "角色信息.xlsx", SysRoleExcel.class, excelList);

    }
}
