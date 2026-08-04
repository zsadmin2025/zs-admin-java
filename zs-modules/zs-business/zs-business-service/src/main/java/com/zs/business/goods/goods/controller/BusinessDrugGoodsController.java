package com.zs.business.goods.goods.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.business.goods.goods.domain.vo.BusinessDrugGoodsVO;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsPageQueryParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsSelectQueryParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsAddParams;
import com.zs.business.goods.goods.domain.params.BusinessDrugGoodsUpdateParams;
import com.zs.business.goods.goods.domain.excel.BusinessDrugGoodsExcel;
import com.zs.business.goods.goods.service.BusinessDrugGoodsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 药品商品主信息表 前端控制器
 * </p>
 *
 * @author zs
 * {@code @date} 2026-07-31 19:22:08
 */
@RestController
@RequestMapping("/business/goods/goods")
public class BusinessDrugGoodsController {

    @Resource
    private BusinessDrugGoodsService businessDrugGoodsService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('goods:goods:page')")
    public Result<PageResult<BusinessDrugGoodsVO>> page(BusinessDrugGoodsPageQueryParams businessDrugGoodsPageQueryParams) {
        PageResult<BusinessDrugGoodsVO> iPage = businessDrugGoodsService.page(businessDrugGoodsPageQueryParams);
        return new Result<PageResult<BusinessDrugGoodsVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('goods:goods:list')")
    public Result<List<BusinessDrugGoodsVO>> list(BusinessDrugGoodsSelectQueryParams businessDrugGoodsSelectQueryParams) {
        List<BusinessDrugGoodsVO> list = businessDrugGoodsService.getList(businessDrugGoodsSelectQueryParams);
        return new Result<List<BusinessDrugGoodsVO>>().ok(list);
    }

    @Log(module = "药品商品主信息表-新增", type = OperationTypeEnum.ADD, description = "新增药品商品主信息表信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('goods:goods:save')")
    public Result<?> save(@RequestBody BusinessDrugGoodsAddParams businessDrugGoodsAddParams) {
        businessDrugGoodsService.save(businessDrugGoodsAddParams);
        return new Result<>().ok();
    }

    @Log(module = "药品商品主信息表-修改", type = OperationTypeEnum.UPDATE, description = "修改药品商品主信息表信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('goods:goods:update')")
    public Result<?> update(@RequestBody BusinessDrugGoodsUpdateParams businessDrugGoodsUpdateParams) {
        businessDrugGoodsService.update(businessDrugGoodsUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('goods:goods:info')")
    public Result<BusinessDrugGoodsVO> get(@PathVariable("id") Long id) {
        BusinessDrugGoodsVO businessDrugGoodsVO = businessDrugGoodsService.getById(id);
        return new Result<BusinessDrugGoodsVO>().ok(businessDrugGoodsVO);
    }


    @Log(module = "药品商品主信息表-删除", type = OperationTypeEnum.DELETE, description = "删除药品商品主信息表信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('goods:goods:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        businessDrugGoodsService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "药品商品主信息表-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除药品商品主信息表信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('goods:goods:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        businessDrugGoodsService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "药品商品主信息表-导出", type = OperationTypeEnum.EXPORT, description = "导出药品商品主信息表信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('goods:goods:export')")
    public void export(HttpServletResponse response, BusinessDrugGoodsSelectQueryParams businessDrugGoodsSelectQueryParams) throws IOException {
        List<BusinessDrugGoodsVO> list = businessDrugGoodsService.getList(businessDrugGoodsSelectQueryParams);
        List<BusinessDrugGoodsExcel> excelList = BeanUtil.copyToList(list, BusinessDrugGoodsExcel.class);
        ExcelUtils.exportExcel(response, "药品商品主信息表.xlsx", BusinessDrugGoodsExcel.class, excelList);

    }
}
