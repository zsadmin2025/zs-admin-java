package com.zs.business.goods.cert.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.cert.domain.vo.BusinessDrugGoodsCertVO;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertPageQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertSelectQueryParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertAddParams;
import com.zs.business.goods.cert.domain.params.BusinessDrugGoodsCertUpdateParams;
import com.zs.business.goods.cert.domain.excel.BusinessDrugGoodsCertExcel;
import com.zs.business.goods.cert.service.BusinessDrugGoodsCertService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 商品证照附件 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-08-03 11:09:18
 */
@RestController
@RequestMapping("/business/goods/cert")
public class BusinessDrugGoodsCertController {

    @Resource
    private BusinessDrugGoodsCertService businessDrugGoodsCertService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('goods:cert:page')")
    public Result<PageResult<BusinessDrugGoodsCertVO>> page(BusinessDrugGoodsCertPageQueryParams businessDrugGoodsCertPageQueryParams) {
        PageResult<BusinessDrugGoodsCertVO> iPage = businessDrugGoodsCertService.page(businessDrugGoodsCertPageQueryParams);
        return new Result<PageResult<BusinessDrugGoodsCertVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('goods:cert:list')")
    public Result<List<BusinessDrugGoodsCertVO>> list(BusinessDrugGoodsCertSelectQueryParams businessDrugGoodsCertSelectQueryParams) {
        List<BusinessDrugGoodsCertVO> list = businessDrugGoodsCertService.getList(businessDrugGoodsCertSelectQueryParams);
        return new Result<List<BusinessDrugGoodsCertVO>>().ok(list);
    }

    @Log(module = "商品证照附件-新增", type = OperationTypeEnum.ADD, description = "新增商品证照附件信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('goods:cert:save')")
    public Result<?> save(@RequestBody BusinessDrugGoodsCertAddParams businessDrugGoodsCertAddParams) {
        businessDrugGoodsCertService.save(businessDrugGoodsCertAddParams);
        return new Result<>().ok();
    }

    @Log(module = "商品证照附件-修改", type = OperationTypeEnum.UPDATE, description = "修改商品证照附件信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('goods:cert:update')")
    public Result<?> update(@RequestBody BusinessDrugGoodsCertUpdateParams businessDrugGoodsCertUpdateParams) {
        businessDrugGoodsCertService.update(businessDrugGoodsCertUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('goods:cert:info')")
    public Result<BusinessDrugGoodsCertVO> get(@PathVariable("id") Long id) {
        BusinessDrugGoodsCertVO businessDrugGoodsCertVO = businessDrugGoodsCertService.getById(id);
        return new Result<BusinessDrugGoodsCertVO>().ok(businessDrugGoodsCertVO);
    }


    @Log(module = "商品证照附件-删除", type = OperationTypeEnum.DELETE, description = "删除商品证照附件信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('goods:cert:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        businessDrugGoodsCertService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "商品证照附件-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除商品证照附件信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('goods:cert:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        businessDrugGoodsCertService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "商品证照附件-导出", type = OperationTypeEnum.EXPORT, description = "导出商品证照附件信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('goods:cert:export')")
    public void export(HttpServletResponse response, BusinessDrugGoodsCertSelectQueryParams businessDrugGoodsCertSelectQueryParams) throws IOException {
        List<BusinessDrugGoodsCertVO> list = businessDrugGoodsCertService.getList(businessDrugGoodsCertSelectQueryParams);
        List<BusinessDrugGoodsCertExcel> excelList = BeanUtil.copyToList(list, BusinessDrugGoodsCertExcel.class);
        ExcelUtils.exportExcel(response, "商品证照附件.xlsx", BusinessDrugGoodsCertExcel.class, excelList);

    }
}
