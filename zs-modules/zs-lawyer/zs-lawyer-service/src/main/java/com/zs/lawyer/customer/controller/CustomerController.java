package com.zs.lawyer.customer.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.customer.domain.excel.CustomerExcel;
import com.zs.lawyer.customer.domain.params.CustomerAddParams;
import com.zs.lawyer.customer.domain.params.CustomerPageQueryParams;
import com.zs.lawyer.customer.domain.params.CustomerSelectQueryParams;
import com.zs.lawyer.customer.domain.params.CustomerUpdateParams;
import com.zs.lawyer.customer.domain.vo.CustomerVO;
import com.zs.lawyer.customer.service.CustomerService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 客户管理 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@RestController
@RequestMapping("/lawyer/customer")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('biz:customer:page')")
    public Result<PageResult<CustomerVO>> page(CustomerPageQueryParams customerPageQueryParams) {
        PageResult<CustomerVO> iPage = customerService.page(customerPageQueryParams);
        return new Result<PageResult<CustomerVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('biz:customer:list')")
    public Result<List<CustomerVO>> list(CustomerSelectQueryParams customerSelectQueryParams) {
        List<CustomerVO> list = customerService.getList(customerSelectQueryParams);
        return new Result<List<CustomerVO>>().ok(list);
    }

    @Log(module = "客户管理-新增", type = OperationTypeEnum.ADD, description = "新增客户管理信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('biz:customer:save')")
    public Result<?> save(@RequestBody CustomerAddParams customerAddParams) {
        customerService.save(customerAddParams);
        return new Result<>().ok();
    }

    @Log(module = "客户管理-修改", type = OperationTypeEnum.UPDATE, description = "修改客户管理信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('biz:customer:update')")
    public Result<?> update(@RequestBody CustomerUpdateParams customerUpdateParams) {
        customerService.update(customerUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('biz:customer:info')")
    public Result<CustomerVO> get(@PathVariable("id") Long id) {
        CustomerVO customerVO = customerService.getById(id);
        return new Result<CustomerVO>().ok(customerVO);
    }


    @Log(module = "客户管理-删除", type = OperationTypeEnum.DELETE, description = "删除客户管理信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('biz:customer:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        customerService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "客户管理-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除客户管理信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('biz:customer:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        customerService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Log(module = "客户管理-导出", type = OperationTypeEnum.EXPORT, description = "导出客户管理信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('biz:customer:export')")
    public void export(HttpServletResponse response, CustomerSelectQueryParams customerSelectQueryParams) throws IOException {
        List<CustomerVO> list = customerService.getList(customerSelectQueryParams);
        List<CustomerExcel> excelList = BeanUtil.copyToList(list, CustomerExcel.class);
        ExcelUtils.exportExcel(response, "客户管理.xlsx", CustomerExcel.class, excelList);

    }
}
