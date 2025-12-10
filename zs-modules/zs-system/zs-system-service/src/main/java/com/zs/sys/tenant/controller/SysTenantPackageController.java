package com.zs.sys.tenant.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.excel.SysTenantPackageExcel;
import com.zs.sys.tenant.domain.params.SysTenantPackageAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPackagePageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantPackageVO;
import com.zs.sys.tenant.service.SysTenantPackageService;
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
 * 租户套餐 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:40
 */
@RestController
@RequestMapping("/system/sys/tenantPackage")
@Tag(name = "租户套餐")
public class SysTenantPackageController {

    @Resource
    private SysTenantPackageService sysTenantPackageService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:tenantPackage:page')")
    public Result<PageResult<SysTenantPackageVO>> page(SysTenantPackagePageQueryParams sysTenantPackagePageQueryParams) {
        PageResult<SysTenantPackageVO> iPage = sysTenantPackageService.page(sysTenantPackagePageQueryParams);
        return new Result<PageResult<SysTenantPackageVO>>().ok(iPage);
    }

    @Operation(summary = "租户套餐列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:tenantPackage:list')")
    public Result<List<SysTenantPackageVO>> list(SysTenantPackageSelectQueryParams sysTenantPackageSelectQueryParams) {
        List<SysTenantPackageVO> list = sysTenantPackageService.getList(sysTenantPackageSelectQueryParams);
        return new Result<List<SysTenantPackageVO>>().ok(list);
    }

    @Operation(summary = "新增租户套餐")
    @Log(module = "租户套餐-新增", type = OperationTypeEnum.ADD, description = "新增租户套餐信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:tenantPackage:save')")
    public Result<?> save(@RequestBody SysTenantPackageAddParams sysTenantPackageAddParams) {
        sysTenantPackageService.save(sysTenantPackageAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改租户套餐")
    @Log(module = "租户套餐-修改", type = OperationTypeEnum.UPDATE, description = "修改租户套餐信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:tenantPackage:update')")
    public Result<?> update(@RequestBody SysTenantPackageUpdateParams sysTenantPackageUpdateParams) {
        sysTenantPackageService.update(sysTenantPackageUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "租户套餐详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:tenantPackage:info')")
    public Result<SysTenantPackageVO> get(@PathVariable("id") Long id) {
        SysTenantPackageVO sysTenantPackageVO = sysTenantPackageService.getById(id);
        return new Result<SysTenantPackageVO>().ok(sysTenantPackageVO);
    }


    @Operation(summary = "删除租户套餐")
    @Log(module = "租户套餐-删除", type = OperationTypeEnum.DELETE, description = "删除租户套餐信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:tenantPackage:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysTenantPackageService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除租户套餐")
    @Log(module = "租户套餐-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除租户套餐信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:tenantPackage:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysTenantPackageService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出租户套餐")
    @Log(module = "租户套餐-导出", type = OperationTypeEnum.EXPORT, description = "导出租户套餐信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:tenantPackage:export')")
    public void export(HttpServletResponse response, SysTenantPackageSelectQueryParams sysTenantPackageSelectQueryParams) throws IOException {
        List<SysTenantPackageVO> list = sysTenantPackageService.getList(sysTenantPackageSelectQueryParams);
        List<SysTenantPackageExcel> excelList = BeanUtil.copyToList(list, SysTenantPackageExcel.class);
        ExcelUtils.exportExcel(response, "租户套餐.xlsx", SysTenantPackageExcel.class, excelList);

    }
}
