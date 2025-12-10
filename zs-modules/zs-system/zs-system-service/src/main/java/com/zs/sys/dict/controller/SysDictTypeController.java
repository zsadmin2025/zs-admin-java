package com.zs.sys.dict.controller;

import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import com.zs.sys.dict.domain.params.SysDictTypeAddParams;
import com.zs.sys.dict.domain.params.SysDictTypeQueryParams;
import com.zs.sys.dict.domain.vo.SysDictTypeVO;
import com.zs.sys.dict.service.ISysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/dictType")
@Tag(name = "字典类型")
public class SysDictTypeController {

    @Resource
    private ISysDictTypeService sysDictTypeService;

    @Operation(summary = "分页查询字典类型")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:dict:page')")
    public Result<PageResult<SysDictTypeVO>> page(SysDictTypeQueryParams sysDictTypeQueryParams) {
        PageResult<SysDictTypeVO> iPage = sysDictTypeService.page(sysDictTypeQueryParams);
        return new Result<PageResult<SysDictTypeVO>>().ok(iPage);
    }

    @Operation(summary = "字典类型列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:dict:list')")
    public Result<List<SysDictTypeVO>> list(SysDictTypeQueryParams sysDictTypeQueryParams) {
        return new Result<List<SysDictTypeVO>>().ok(sysDictTypeService.list(sysDictTypeQueryParams));
    }

    @Operation(summary = "新增字典类型")
    @Log(module = "字典类型-新增", type = OperationTypeEnum.ADD, description = "新增字典类型")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:dict:save')")
    public Result<?> save(@Valid  @RequestBody SysDictTypeAddParams sysDictTypeAddParams) {

        sysDictTypeService.save(sysDictTypeAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改字典类型")
    @Log(module = "字典类型-修改", type = OperationTypeEnum.UPDATE, description = "修改字典类型")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:dict:update')")
    public Result<?> update(@Valid @RequestBody SysDictTypeAddParams sysDictTypeAddParams) {
        sysDictTypeService.update(sysDictTypeAddParams);
        return new Result<>().ok();
    }


    @Operation(summary = "字典类型详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:dict:info')")
    public Result<SysDictTypeVO> get(@PathVariable("id") Long id) {
        SysDictTypeVO sysDictTypeVO = sysDictTypeService.getById(id);
        return new Result<SysDictTypeVO>().ok(sysDictTypeVO);
    }


    @Operation(summary = "删除字典类型")
    @Log(module = "字典类型-删除", type = OperationTypeEnum.DELETE, description = "删除字典类型")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:dict:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysDictTypeService.deleteById(id);
        return new Result<>().ok();
    }

}
