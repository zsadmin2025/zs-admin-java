package com.zs.business.warehouse.warehouse.info.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.business.warehouse.warehouse.info.domain.vo.BusinessWarehouseInfoVO;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoPageQueryParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoSelectQueryParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoAddParams;
import com.zs.business.warehouse.warehouse.info.domain.params.BusinessWarehouseInfoUpdateParams;
import com.zs.business.warehouse.warehouse.info.domain.excel.BusinessWarehouseInfoExcel;
import com.zs.business.warehouse.warehouse.info.service.BusinessWarehouseInfoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 库房表 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-04 11:38:39
 */
@RestController
@RequestMapping("/business/warehouse/info")
public class BusinessWarehouseInfoController {

    @Resource
    private BusinessWarehouseInfoService businessWarehouseInfoService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('warehouse:info:page')")
    public Result<PageResult<BusinessWarehouseInfoVO>> page(BusinessWarehouseInfoPageQueryParams businessWarehouseInfoPageQueryParams) {
        PageResult<BusinessWarehouseInfoVO> iPage = businessWarehouseInfoService.page(businessWarehouseInfoPageQueryParams);
        return new Result<PageResult<BusinessWarehouseInfoVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('warehouse:info:list')")
    public Result<List<BusinessWarehouseInfoVO>> list(BusinessWarehouseInfoSelectQueryParams businessWarehouseInfoSelectQueryParams) {
        List<BusinessWarehouseInfoVO> list = businessWarehouseInfoService.getList(businessWarehouseInfoSelectQueryParams);
        return new Result<List<BusinessWarehouseInfoVO>>().ok(list);
    }

    @Log(module = "库房表-新增", type = OperationTypeEnum.ADD, description = "新增库房表信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('warehouse:info:save')")
    public Result<?> save(@RequestBody BusinessWarehouseInfoAddParams businessWarehouseInfoAddParams) {
        businessWarehouseInfoService.save(businessWarehouseInfoAddParams);
        return new Result<>().ok();
    }

    @Log(module = "库房表-修改", type = OperationTypeEnum.UPDATE, description = "修改库房表信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('warehouse:info:update')")
    public Result<?> update(@RequestBody BusinessWarehouseInfoUpdateParams businessWarehouseInfoUpdateParams) {
        businessWarehouseInfoService.update(businessWarehouseInfoUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('warehouse:info:info')")
    public Result<BusinessWarehouseInfoVO> get(@PathVariable("id") Long id) {
        BusinessWarehouseInfoVO businessWarehouseInfoVO = businessWarehouseInfoService.getById(id);
        return new Result<BusinessWarehouseInfoVO>().ok(businessWarehouseInfoVO);
    }


    @Log(module = "库房表-删除", type = OperationTypeEnum.DELETE, description = "删除库房表信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('warehouse:info:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        businessWarehouseInfoService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "库房表-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除库房表信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('warehouse:info:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        businessWarehouseInfoService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "库房表-导出", type = OperationTypeEnum.EXPORT, description = "导出库房表信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('warehouse:info:export')")
    public void export(HttpServletResponse response, BusinessWarehouseInfoSelectQueryParams businessWarehouseInfoSelectQueryParams) throws IOException {
        List<BusinessWarehouseInfoVO> list = businessWarehouseInfoService.getList(businessWarehouseInfoSelectQueryParams);
        List<BusinessWarehouseInfoExcel> excelList = BeanUtil.copyToList(list, BusinessWarehouseInfoExcel.class);
        ExcelUtils.exportExcel(response, "库房表.xlsx", BusinessWarehouseInfoExcel.class, excelList);

    }
}
