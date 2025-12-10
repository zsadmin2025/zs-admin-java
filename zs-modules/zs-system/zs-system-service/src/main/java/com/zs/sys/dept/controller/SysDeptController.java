package com.zs.sys.dept.controller;


import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.MyTreeNode;
import com.zs.sys.dept.domain.params.SysDeptAddParams;
import com.zs.sys.dept.domain.params.SysDeptPageQueryParams;
import com.zs.sys.dept.domain.params.SysDeptQueryParams;
import com.zs.sys.dept.domain.params.SysDeptUpdateParams;
import com.zs.sys.dept.domain.vo.SysDeptTreeVO;
import com.zs.sys.dept.domain.vo.SysDeptVO;
import com.zs.sys.dept.service.ISysDeptService;
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
@RequestMapping("system/sys/dept")
@Tag(name = "部门管理")
public class SysDeptController {

    @Resource
    private ISysDeptService iSysDeptService;


    @Operation(summary = "部门树")
    @GetMapping("tree")
    @PreAuthorize("hasAuthority('sys:dept:tree')")
    public Result<List<SysDeptTreeVO>> list(SysDeptQueryParams sysOrgQueryParams) {
        List<SysDeptTreeVO> list = iSysDeptService.getTree(sysOrgQueryParams);
        return new Result<List<SysDeptTreeVO>>().ok(list);
    }

    @Operation(summary = "分页查询部门")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:dept:page')")
    public Result<PageResult<SysDeptVO>> page(SysDeptPageQueryParams sysDeptPageQueryParams) {
        PageResult<SysDeptVO> iPage = iSysDeptService.page(sysDeptPageQueryParams);
        return new Result<PageResult<SysDeptVO>>().ok(iPage);
    }

    @Operation(summary = "新增部门")
    @Log(module = "部门管理-新增", type = OperationTypeEnum.ADD, description = "新增部门信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:dept:save')")
    public Result<?> save(@Valid @RequestBody SysDeptAddParams sysOrgAddParams) {
        iSysDeptService.save(sysOrgAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改部门")
    @Log(module = "部门管理-修改", type = OperationTypeEnum.UPDATE, description = "修改部门信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:dept:update')")
    public Result<?> update(@Valid @RequestBody SysDeptUpdateParams sysDeptUpdateParams) {
        iSysDeptService.update(sysDeptUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "查询部门信息")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:dept:info')")
    public Result<SysDeptVO> get(@PathVariable("id") Long id) {
        SysDeptVO sysOrgVO = iSysDeptService.getById(id);
        return new Result<SysDeptVO>().ok(sysOrgVO);
    }

    @Operation(summary = "删除部门")
    @Log(module = "部门管理-删除", type = OperationTypeEnum.DELETE, description = "删除部门信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:dept:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        iSysDeptService.removeById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "部门列表")
    @GetMapping("list")
    public Result<List<SysDeptVO>> list() {
        List<SysDeptVO> list = iSysDeptService.getList(null);
        return new Result<List<SysDeptVO>>().ok(list);
    }

    @Operation(summary = "部门岗位树形")
    @GetMapping("getDeptPostTree")
    public Result<List<MyTreeNode>> deptPostTree() {
        List<MyTreeNode> list = iSysDeptService.getDeptPostTree();
        return new Result<List<MyTreeNode>>().ok(list);
    }
}
