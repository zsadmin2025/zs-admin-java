package com.zs.business.partner.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.business.partner.category.domain.vo.BusinessPartnerCategoryVO;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryPageQueryParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategorySelectQueryParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryAddParams;
import com.zs.business.partner.category.domain.params.BusinessPartnerCategoryUpdateParams;
import com.zs.business.partner.category.domain.excel.BusinessPartnerCategoryExcel;
import com.zs.business.partner.category.service.BusinessPartnerCategoryService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 单位分类 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 09:15:47
 */
@RestController
@RequestMapping("/business/partner/category")
public class BusinessPartnerCategoryController {

    @Resource
    private BusinessPartnerCategoryService businessPartnerCategoryService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('partner:category:page')")
    public Result<PageResult<BusinessPartnerCategoryVO>> page(BusinessPartnerCategoryPageQueryParams businessPartnerCategoryPageQueryParams) {
        PageResult<BusinessPartnerCategoryVO> iPage = businessPartnerCategoryService.page(businessPartnerCategoryPageQueryParams);
        return new Result<PageResult<BusinessPartnerCategoryVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('partner:category:list')")
    public Result<List<BusinessPartnerCategoryVO>> list(BusinessPartnerCategorySelectQueryParams businessPartnerCategorySelectQueryParams) {
        List<BusinessPartnerCategoryVO> list = businessPartnerCategoryService.getList(businessPartnerCategorySelectQueryParams);
        return new Result<List<BusinessPartnerCategoryVO>>().ok(list);
    }

    @Log(module = "单位分类-新增", type = OperationTypeEnum.ADD, description = "新增单位分类信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('partner:category:save')")
    public Result<?> save(@RequestBody BusinessPartnerCategoryAddParams businessPartnerCategoryAddParams) {
        businessPartnerCategoryService.save(businessPartnerCategoryAddParams);
        return new Result<>().ok();
    }

    @Log(module = "单位分类-修改", type = OperationTypeEnum.UPDATE, description = "修改单位分类信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('partner:category:update')")
    public Result<?> update(@RequestBody BusinessPartnerCategoryUpdateParams businessPartnerCategoryUpdateParams) {
        businessPartnerCategoryService.update(businessPartnerCategoryUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('partner:category:info')")
    public Result<BusinessPartnerCategoryVO> get(@PathVariable("id") Long id) {
        BusinessPartnerCategoryVO businessPartnerCategoryVO = businessPartnerCategoryService.getById(id);
        return new Result<BusinessPartnerCategoryVO>().ok(businessPartnerCategoryVO);
    }


    @Log(module = "单位分类-删除", type = OperationTypeEnum.DELETE, description = "删除单位分类信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('partner:category:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        businessPartnerCategoryService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "单位分类-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除单位分类信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('partner:category:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        businessPartnerCategoryService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "单位分类-导出", type = OperationTypeEnum.EXPORT, description = "导出单位分类信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('partner:category:export')")
    public void export(HttpServletResponse response, BusinessPartnerCategorySelectQueryParams businessPartnerCategorySelectQueryParams) throws IOException {
        List<BusinessPartnerCategoryVO> list = businessPartnerCategoryService.getList(businessPartnerCategorySelectQueryParams);
        List<BusinessPartnerCategoryExcel> excelList = BeanUtil.copyToList(list, BusinessPartnerCategoryExcel.class);
        ExcelUtils.exportExcel(response, "单位分类.xlsx", BusinessPartnerCategoryExcel.class, excelList);

    }
}
