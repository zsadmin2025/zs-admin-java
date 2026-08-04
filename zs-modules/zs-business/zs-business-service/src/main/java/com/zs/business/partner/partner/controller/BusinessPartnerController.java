package com.zs.business.partner.partner.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.business.partner.partner.domain.vo.BusinessPartnerVO;
import com.zs.business.partner.partner.domain.params.BusinessPartnerPageQueryParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerSelectQueryParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerAddParams;
import com.zs.business.partner.partner.domain.params.BusinessPartnerUpdateParams;
import com.zs.business.partner.partner.domain.excel.BusinessPartnerExcel;
import com.zs.business.partner.partner.service.BusinessPartnerService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 往来单位 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-02 11:23:09
 */
@RestController
@RequestMapping("/business/partner/partner")
public class BusinessPartnerController {

    @Resource
    private BusinessPartnerService businessPartnerService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('partner:partner:page')")
    public Result<PageResult<BusinessPartnerVO>> page(BusinessPartnerPageQueryParams businessPartnerPageQueryParams) {
        PageResult<BusinessPartnerVO> iPage = businessPartnerService.page(businessPartnerPageQueryParams);
        return new Result<PageResult<BusinessPartnerVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('partner:partner:list')")
    public Result<List<BusinessPartnerVO>> list(BusinessPartnerSelectQueryParams businessPartnerSelectQueryParams) {
        List<BusinessPartnerVO> list = businessPartnerService.getList(businessPartnerSelectQueryParams);
        return new Result<List<BusinessPartnerVO>>().ok(list);
    }

    @Log(module = "往来单位-新增", type = OperationTypeEnum.ADD, description = "新增往来单位信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('partner:partner:save')")
    public Result<?> save(@RequestBody BusinessPartnerAddParams businessPartnerAddParams) {
        businessPartnerService.save(businessPartnerAddParams);
        return new Result<>().ok();
    }

    @Log(module = "往来单位-修改", type = OperationTypeEnum.UPDATE, description = "修改往来单位信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('partner:partner:update')")
    public Result<?> update(@RequestBody BusinessPartnerUpdateParams businessPartnerUpdateParams) {
        businessPartnerService.update(businessPartnerUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('partner:partner:info')")
    public Result<BusinessPartnerVO> get(@PathVariable("id") Long id) {
        BusinessPartnerVO businessPartnerVO = businessPartnerService.getById(id);
        return new Result<BusinessPartnerVO>().ok(businessPartnerVO);
    }


    @Log(module = "往来单位-删除", type = OperationTypeEnum.DELETE, description = "删除往来单位信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('partner:partner:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        businessPartnerService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "往来单位-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除往来单位信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('partner:partner:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        businessPartnerService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "往来单位-导出", type = OperationTypeEnum.EXPORT, description = "导出往来单位信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('partner:partner:export')")
    public void export(HttpServletResponse response, BusinessPartnerSelectQueryParams businessPartnerSelectQueryParams) throws IOException {
        List<BusinessPartnerVO> list = businessPartnerService.getList(businessPartnerSelectQueryParams);
        List<BusinessPartnerExcel> excelList = BeanUtil.copyToList(list, BusinessPartnerExcel.class);
        ExcelUtils.exportExcel(response, "往来单位.xlsx", BusinessPartnerExcel.class, excelList);

    }
}
