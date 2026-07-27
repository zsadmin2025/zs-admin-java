package com.zs.sys.demo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.demo.domain.excel.SysDemoExcel;
import com.zs.sys.demo.domain.params.SysDemoAddParams;
import com.zs.sys.demo.domain.params.SysDemoPageQueryParams;
import com.zs.sys.demo.domain.params.SysDemoSelectQueryParams;
import com.zs.sys.demo.domain.params.SysDemoUpdateParams;
import com.zs.sys.demo.domain.vo.SysDemoVO;
import com.zs.sys.demo.service.SysDemoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 代码生成测试表 前端控制器
 * </p>
 *
 * @author zs
 * @date 2026-01-07 11:01:19
 */
@RestController
@RequestMapping("/sys/demo")
public class SysDemoController {

    @Resource
    private SysDemoService sysDemoService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:demo:page')")
    public Result<PageResult<SysDemoVO>> page(SysDemoPageQueryParams sysDemoPageQueryParams) {
        PageResult<SysDemoVO> iPage = sysDemoService.page(sysDemoPageQueryParams);
        return new Result<PageResult<SysDemoVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:demo:list')")
    public Result<List<SysDemoVO>> list(SysDemoSelectQueryParams sysDemoSelectQueryParams) {
        List<SysDemoVO> list = sysDemoService.getList(sysDemoSelectQueryParams);
        return new Result<List<SysDemoVO>>().ok(list);
    }

    @Log(module = "代码生成测试表-新增", type = OperationTypeEnum.ADD, description = "新增代码生成测试表信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:demo:save')")
    public Result<?> save(@RequestBody SysDemoAddParams sysDemoAddParams) {
		sysDemoService.save(sysDemoAddParams);
        return new Result<>().ok();
    }

    @Log(module = "代码生成测试表-修改", type = OperationTypeEnum.UPDATE, description = "修改代码生成测试表信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:demo:update')")
    public Result<?> update(@RequestBody SysDemoUpdateParams sysDemoUpdateParams) {
		sysDemoService.update(sysDemoUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:demo:info')")
    public Result<SysDemoVO> get(@PathVariable("id") Long id) {
		SysDemoVO sysDemoVO = sysDemoService.getById(id);
        return new Result<SysDemoVO>().ok(sysDemoVO);
    }


    @Log(module = "代码生成测试表-删除", type = OperationTypeEnum.DELETE, description = "删除代码生成测试表信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:demo:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
		sysDemoService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "代码生成测试表-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除代码生成测试表信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:demo:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
		sysDemoService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "代码生成测试表-导出", type = OperationTypeEnum.EXPORT, description = "导出代码生成测试表信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:demo:export')")
    public void export(HttpServletResponse response, SysDemoSelectQueryParams sysDemoSelectQueryParams) throws IOException {
        List<SysDemoVO> list = sysDemoService.getList(sysDemoSelectQueryParams);
        List<SysDemoExcel> excelList = BeanUtil.copyToList(list, SysDemoExcel.class);
        ExcelUtils.exportExcel(response, "代码生成测试表.xlsx", SysDemoExcel.class, excelList);

    }
}
