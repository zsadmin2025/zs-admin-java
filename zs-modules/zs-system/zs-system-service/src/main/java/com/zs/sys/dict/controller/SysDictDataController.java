package com.zs.sys.dict.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.dict.domain.excel.SysDictDataExcel;
import com.zs.sys.dict.domain.params.SysDictDataAddParams;
import com.zs.sys.dict.domain.params.SysDictDataPageQueryParams;
import com.zs.sys.dict.domain.params.SysDictDataSelectQueryParams;
import com.zs.sys.dict.domain.params.SysDictDataUpdateParams;
import com.zs.sys.dict.domain.vo.SysDictDataVO;
import com.zs.sys.dict.service.ISysDictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/dictData")
@Tag(name = "字典数据")
public class SysDictDataController {

    @Resource
    private ISysDictDataService sysDictDataService;

    @Operation(summary = "分页查询字典数据")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:dict:page')")
    public Result<PageResult<SysDictDataVO>> page(SysDictDataPageQueryParams sysDictDataPageQueryParams) {

        PageResult<SysDictDataVO> iPage = sysDictDataService.page(sysDictDataPageQueryParams);
        return new Result<PageResult<SysDictDataVO>>().ok(iPage);
    }

    @Operation(summary = "字典数据列表")
    @GetMapping("list")
//    @PreAuthorize("hasAuthority('sys:dict:list')")
    public Result<Map<String, List<SysDictDataVO>>> list() {
        Map<String, List<SysDictDataVO>> list = sysDictDataService.getList();
        return new Result<Map<String, List<SysDictDataVO>>>().ok(list);
    }

    @Operation(summary = "查询指定字典类型下的数据列表")
    @GetMapping("dictType/list")
//    @PreAuthorize("hasAuthority('sys:dict:list')")
    public Result<List<SysDictDataVO>> dictTypeList(SysDictDataSelectQueryParams sysDictDataSelectQueryParams) {
        List<SysDictDataVO> list = sysDictDataService.dictTypeList(sysDictDataSelectQueryParams);

        return new Result<List<SysDictDataVO>>().ok(list);
    }

    @Operation(summary = "新增字典数据")
    @PostMapping("save")
    @Log(module = "字典数据-新增", type = OperationTypeEnum.ADD, description = "新增字典数据")
    @PreAuthorize("hasAuthority('sys:dict:save')")
    public Result<?> save(@Valid  @RequestBody SysDictDataAddParams sysDictDataAddParams) {
        sysDictDataService.save(sysDictDataAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改字典数据")
    @PutMapping("update")
    @Log(module = "字典数据-修改", type = OperationTypeEnum.UPDATE, description = "修改字典数据")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    public Result<?> update(@Valid @RequestBody SysDictDataUpdateParams sysDictDataUpdateParams) {
        sysDictDataService.update(sysDictDataUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "字典数据详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:dict:info')")
    public Result<SysDictDataVO> get(@PathVariable("id") Long id) {
        return new Result<SysDictDataVO>().ok(sysDictDataService.getById(id));
    }

    @Operation(summary = "删除字典数据")
    @Log(module = "字典数据-删除", type = OperationTypeEnum.DELETE, description = "删除字典数据")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysDictDataService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除字典数据")
    @Log(module = "字典数据-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除字典数据")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:dict:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysDictDataService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出字典数据")
    @Log(module = "字典数据-导出", type = OperationTypeEnum.EXPORT, description = "导出字典数据信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:dict:export')")
    public void export(HttpServletResponse response, SysDictDataSelectQueryParams sysDictDataQueryParams) throws IOException {
        List<SysDictDataVO> list = sysDictDataService.dictTypeList(sysDictDataQueryParams);
        List<SysDictDataExcel> excelList = BeanUtil.copyToList(list, SysDictDataExcel.class);
        ExcelUtils.exportExcel(response, "字典信息.xlsx", SysDictDataExcel.class, excelList);

    }
}
