package com.zs.sys.tenant.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantEntity;
import com.zs.sys.tenant.domain.excel.SysTenantExcel;
import com.zs.sys.tenant.domain.params.SysTenantAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantSelectVO;
import com.zs.sys.tenant.domain.vo.SysTenantVO;
import com.zs.sys.tenant.service.SysTenantService;
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
 * 租户管理 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:45
 */
@RestController
@RequestMapping("/system/sys/tenant")
@Tag(name = "租户管理")
public class SysTenantController {

    @Resource
    private SysTenantService sysTenantService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:tenant:page')")
    public Result<PageResult<SysTenantVO>> page(SysTenantPageQueryParams sysTenantPageQueryParams) {
        PageResult<SysTenantVO> iPage = sysTenantService.page(sysTenantPageQueryParams);
        return new Result<PageResult<SysTenantVO>>().ok(iPage);
    }

    @Operation(summary = "租户信息下拉选")
    @GetMapping("select")
    public Result<List<SysTenantSelectVO>> select() {
        List<SysTenantEntity> list = sysTenantService.list(new LambdaQueryWrapper<SysTenantEntity>().eq(SysTenantEntity::getStatus, 1));
        List<SysTenantSelectVO> dtoList = BeanUtil.copyToList(list, SysTenantSelectVO.class);
        return new Result<List<SysTenantSelectVO>>().ok(dtoList);
    }

    @Operation(summary = "租户信息列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:tenant:list')")
    public Result<List<SysTenantVO>> list(SysTenantSelectQueryParams sysTenantSelectQueryParams) {
        List<SysTenantVO> list = sysTenantService.getList(sysTenantSelectQueryParams);
        return new Result<List<SysTenantVO>>().ok(list);
    }

    @Operation(summary = "新增租户信息")
    @Log(module = "租户管理-新增", type = OperationTypeEnum.ADD, description = "新增租户管理信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:tenant:save')")
    public Result<?> save(@RequestBody SysTenantAddParams sysTenantAddParams) {
        sysTenantService.save(sysTenantAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改租户信息")
    @Log(module = "租户管理-修改", type = OperationTypeEnum.UPDATE, description = "修改租户管理信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:tenant:update')")
    public Result<?> update(@RequestBody SysTenantUpdateParams sysTenantUpdateParams) {
        sysTenantService.update(sysTenantUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "租户信息详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:tenant:info')")
    public Result<SysTenantVO> get(@PathVariable("id") Long id) {
        SysTenantVO sysTenantVO = sysTenantService.getById(id);
        return new Result<SysTenantVO>().ok(sysTenantVO);
    }

    @Operation(summary = "删除租户信息")
    @Log(module = "租户管理-删除", type = OperationTypeEnum.DELETE, description = "删除租户管理信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:tenant:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysTenantService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除租户信息")
    @Log(module = "租户管理-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除租户管理信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:tenant:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysTenantService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出租户信息")
    @Log(module = "租户管理-导出", type = OperationTypeEnum.EXPORT, description = "导出租户管理信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:tenant:export')")
    public void export(HttpServletResponse response, SysTenantSelectQueryParams sysTenantSelectQueryParams) throws IOException {
        List<SysTenantVO> list = sysTenantService.getList(sysTenantSelectQueryParams);
        List<SysTenantExcel> excelList = BeanUtil.copyToList(list, SysTenantExcel.class);
        ExcelUtils.exportExcel(response, "租户管理.xlsx", SysTenantExcel.class, excelList);

    }
}
