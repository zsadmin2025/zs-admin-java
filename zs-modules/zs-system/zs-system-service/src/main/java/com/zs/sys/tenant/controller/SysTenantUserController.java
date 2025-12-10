package com.zs.sys.tenant.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.excel.SysTenantUserExcel;
import com.zs.sys.tenant.domain.params.SysTenantUserAddParams;
import com.zs.sys.tenant.domain.params.SysTenantUserPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUserSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUserUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantUserVO;
import com.zs.sys.tenant.service.SysTenantUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 租户用户 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:38
 */
@RestController
@RequestMapping("/system/sys/tenantUser")
@Tag(name = "租户用户")
public class SysTenantUserController {

    @Resource
    private SysTenantUserService sysTenantUserService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:tenantUser:page')")
    public Result<PageResult<SysTenantUserVO>> page(SysTenantUserPageQueryParams sysTenantUserPageQueryParams) {
        PageResult<SysTenantUserVO> iPage = sysTenantUserService.page(sysTenantUserPageQueryParams);
        return new Result<PageResult<SysTenantUserVO>>().ok(iPage);
    }

    @Operation(summary = "租户用户列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:tenantUser:list')")
    public Result<List<SysTenantUserVO>> list(SysTenantUserSelectQueryParams sysTenantUserSelectQueryParams) {
        List<SysTenantUserVO> list = sysTenantUserService.getList(sysTenantUserSelectQueryParams);
        return new Result<List<SysTenantUserVO>>().ok(list);
    }

    @Operation(summary = "新增租户用户")
    @Log(module = "租户用户-新增", type = OperationTypeEnum.ADD, description = "新增租户用户信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:tenantUser:save')")
    public Result<?> save(@RequestBody SysTenantUserAddParams sysTenantUserAddParams) {
				sysTenantUserService.save(sysTenantUserAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改租户用户")
    @Log(module = "租户用户-修改", type = OperationTypeEnum.UPDATE, description = "修改租户用户信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:tenantUser:update')")
    public Result<?> update(@RequestBody SysTenantUserUpdateParams sysTenantUserUpdateParams) {
				sysTenantUserService.update(sysTenantUserUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "租户用户详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:tenantUser:info')")
    public Result<SysTenantUserVO> get(@PathVariable("id") Long id) {
				SysTenantUserVO sysTenantUserVO = sysTenantUserService.getById(id);
        return new Result<SysTenantUserVO>().ok(sysTenantUserVO);
    }


    @Operation(summary = "删除租户用户")
    @Log(module = "租户用户-删除", type = OperationTypeEnum.DELETE, description = "删除租户用户信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:tenantUser:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				sysTenantUserService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除租户用户")
    @Log(module = "租户用户-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除租户用户信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:tenantUser:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				sysTenantUserService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出租户用户")
    @Log(module = "租户用户-导出", type = OperationTypeEnum.EXPORT, description = "导出租户用户信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:tenantUser:export')")
    public void export(HttpServletResponse response, SysTenantUserSelectQueryParams sysTenantUserSelectQueryParams) throws IOException {
        List<SysTenantUserVO> list = sysTenantUserService.getList(sysTenantUserSelectQueryParams);
        List<SysTenantUserExcel> excelList = BeanUtil.copyToList(list, SysTenantUserExcel.class);
        ExcelUtils.exportExcel(response, "租户用户.xlsx", SysTenantUserExcel.class, excelList);

    }
}
