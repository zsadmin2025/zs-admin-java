package com.zs.sys.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.MyTreeNode;
import com.zs.sys.post.domain.excel.SysPostExcel;
import com.zs.sys.post.domain.params.SysPostAddParams;
import com.zs.sys.post.domain.params.SysPostQueryParams;
import com.zs.sys.post.domain.vo.SysPostVO;
import com.zs.sys.post.service.ISysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/post")
@Tag(name = "岗位管理")
public class SysPostController {

    @Resource
    private ISysPostService iSysPostService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:post:page')")
    public Result<PageResult<SysPostVO>> page(SysPostQueryParams sysPostQueryParams) {
        PageResult<SysPostVO> iPage = iSysPostService.page(sysPostQueryParams);
        return new Result<PageResult<SysPostVO>>().ok(iPage);
    }

    @Operation(summary = "岗位列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:post:list')")
    public Result<List<SysPostVO>> list(SysPostQueryParams sysPostQueryParams) {
        List<SysPostVO> list = iSysPostService.getList(sysPostQueryParams);
        return new Result<List<SysPostVO>>().ok(list);
    }

    @Operation(summary = "新增岗位信息")
    @Log(module = "岗位管理-新增", type = OperationTypeEnum.ADD, description = "新增岗位信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:post:save')")
    public Result<?> save(@RequestBody SysPostAddParams sysPostAddParams) {

        iSysPostService.save(sysPostAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改岗位信息")
    @Log(module = "岗位管理-修改", type = OperationTypeEnum.UPDATE, description = "修改岗位信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:post:update')")
    public Result<?> update(@RequestBody SysPostAddParams sysPostAddParams) {
        iSysPostService.update(sysPostAddParams);
        return new Result<>().ok();
    }


    @Operation(summary = "岗位详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:post:info')")
    public Result<SysPostVO> get(@PathVariable("id") Long id) {
        SysPostVO sysOrgVO = iSysPostService.getById(id);
        return new Result<SysPostVO>().ok(sysOrgVO);
    }

    @Operation(summary = "删除岗位信息")
    @Log(module = "岗位管理-删除", type = OperationTypeEnum.DELETE, description = "删除岗位信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:post:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        iSysPostService.delById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除岗位信息")
    @Log(module = "岗位管理-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除岗位信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:post:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        iSysPostService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出岗位信息")
    @Log(module = "岗位管理-导出", type = OperationTypeEnum.EXPORT, description = "导出岗位信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:post:export')")
    public void export(HttpServletResponse response, SysPostQueryParams sysPostQueryParams) throws IOException {
        List<SysPostVO> list = iSysPostService.getList(sysPostQueryParams);
        List<SysPostExcel> excelList = BeanUtil.copyToList(list, SysPostExcel.class);
        ExcelUtils.exportExcel(response, "岗位信息.xlsx", SysPostExcel.class, excelList);
    }

    @Operation(summary = "部门岗位树形")
    @GetMapping("getDeptPostTree")
    public Result<List<MyTreeNode>> deptPostTree() {
        List<MyTreeNode> list = iSysPostService.getDeptPostTree();
        return new Result<List<MyTreeNode>>().ok(list);
    }
}
