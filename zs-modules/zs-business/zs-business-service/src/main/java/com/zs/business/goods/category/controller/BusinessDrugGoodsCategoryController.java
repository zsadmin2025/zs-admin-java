package com.zs.business.goods.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.category.domain.vo.BusinessDrugGoodsCategoryVO;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryPageQueryParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategorySelectQueryParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryAddParams;
import com.zs.business.goods.category.domain.params.BusinessDrugGoodsCategoryUpdateParams;
import com.zs.business.goods.category.domain.excel.BusinessDrugGoodsCategoryExcel;
import com.zs.business.goods.category.service.BusinessDrugGoodsCategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 商品档案 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:03
 */
@RestController
@RequestMapping("/business/goods/category")
public class BusinessDrugGoodsCategoryController {

    @Resource
    private BusinessDrugGoodsCategoryService businessDrugGoodsCategoryService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('goods:category:page')")
    public Result<PageResult<BusinessDrugGoodsCategoryVO>> page(BusinessDrugGoodsCategoryPageQueryParams businessDrugGoodsCategoryPageQueryParams) {
        PageResult<BusinessDrugGoodsCategoryVO> iPage = businessDrugGoodsCategoryService.page(businessDrugGoodsCategoryPageQueryParams);
        return new Result<PageResult<BusinessDrugGoodsCategoryVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('goods:category:list')")
    public Result<List<BusinessDrugGoodsCategoryVO>> list(BusinessDrugGoodsCategorySelectQueryParams businessDrugGoodsCategorySelectQueryParams) {
        List<BusinessDrugGoodsCategoryVO> list = businessDrugGoodsCategoryService.getList(businessDrugGoodsCategorySelectQueryParams);
        return new Result<List<BusinessDrugGoodsCategoryVO>>().ok(list);
    }

    @Log(module = "商品档案-新增", type = OperationTypeEnum.ADD, description = "新增商品档案信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('goods:category:save')")
    public Result<?> save(@RequestBody BusinessDrugGoodsCategoryAddParams businessDrugGoodsCategoryAddParams) {
        businessDrugGoodsCategoryService.save(businessDrugGoodsCategoryAddParams);
        return new Result<>().ok();
    }

    @Log(module = "商品档案-修改", type = OperationTypeEnum.UPDATE, description = "修改商品档案信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('goods:category:update')")
    public Result<?> update(@RequestBody BusinessDrugGoodsCategoryUpdateParams businessDrugGoodsCategoryUpdateParams) {
        businessDrugGoodsCategoryService.update(businessDrugGoodsCategoryUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('goods:category:info')")
    public Result<BusinessDrugGoodsCategoryVO> get(@PathVariable("id") Long id) {
        BusinessDrugGoodsCategoryVO businessDrugGoodsCategoryVO = businessDrugGoodsCategoryService.getById(id);
        return new Result<BusinessDrugGoodsCategoryVO>().ok(businessDrugGoodsCategoryVO);
    }


    @Log(module = "商品档案-删除", type = OperationTypeEnum.DELETE, description = "删除商品档案信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('goods:category:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        businessDrugGoodsCategoryService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "商品档案-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除商品档案信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('goods:category:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        businessDrugGoodsCategoryService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "商品档案-导出", type = OperationTypeEnum.EXPORT, description = "导出商品档案信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('goods:category:export')")
    public void export(HttpServletResponse response, BusinessDrugGoodsCategorySelectQueryParams businessDrugGoodsCategorySelectQueryParams) throws IOException {
        List<BusinessDrugGoodsCategoryVO> list = businessDrugGoodsCategoryService.getList(businessDrugGoodsCategorySelectQueryParams);
        List<BusinessDrugGoodsCategoryExcel> excelList = BeanUtil.copyToList(list, BusinessDrugGoodsCategoryExcel.class);
        ExcelUtils.exportExcel(response, "商品档案.xlsx", BusinessDrugGoodsCategoryExcel.class, excelList);

    }
}
