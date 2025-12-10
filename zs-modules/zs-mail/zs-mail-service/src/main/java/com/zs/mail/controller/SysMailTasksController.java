package com.zs.mail.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.mail.domain.excel.SysMailTasksExcel;
import com.zs.mail.domain.params.SysMailTasksAddParams;
import com.zs.mail.domain.params.SysMailTasksPageQueryParams;
import com.zs.mail.domain.params.SysMailTasksSelectQueryParams;
import com.zs.mail.domain.params.SysMailTasksUpdateParams;
import com.zs.mail.domain.vo.SysMailTasksVO;
import com.zs.mail.service.SysMailTasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 邮件任务 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-10-30 11:13:31
 */
@RestController
@RequestMapping("/system/sys/tasks")
@Tag(name = "邮件任务")
public class SysMailTasksController {

    @Resource
    private SysMailTasksService sysMailTasksService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:mailTasks:page')")
    public Result<PageResult<SysMailTasksVO>> page(SysMailTasksPageQueryParams sysMailTasksPageQueryParams) {
        PageResult<SysMailTasksVO> iPage = sysMailTasksService.page(sysMailTasksPageQueryParams);
        return new Result<PageResult<SysMailTasksVO>>().ok(iPage);
    }

    @Operation(summary = "列表查询")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:mailTasks:list')")
    public Result<List<SysMailTasksVO>> list(SysMailTasksSelectQueryParams sysMailTasksSelectQueryParams) {
        List<SysMailTasksVO> list = sysMailTasksService.getList(sysMailTasksSelectQueryParams);
        return new Result<List<SysMailTasksVO>>().ok(list);
    }

    @Operation(summary = "新增")
    @Log(module = "邮件任务-新增", type = OperationTypeEnum.ADD, description = "新增邮件任务信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:mailTasks:save')")
    public Result<?> save(@Valid  @RequestBody SysMailTasksAddParams sysMailTasksAddParams) {
        sysMailTasksService.save(sysMailTasksAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "立即发送")
    @Log(module = "邮件任务-立即发送", type = OperationTypeEnum.ADD, description = "立即发送邮件任务信息")
    @PostMapping("sendNow")
    @PreAuthorize("hasAuthority('sys:mailTasks:sendNow')")
    public Result<?> sendNow(@Valid @RequestBody SysMailTasksAddParams sysMailTasksAddParams) {
        sysMailTasksService.sendNow(sysMailTasksAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "发送")
    @Log(module = "邮件任务-发送", type = OperationTypeEnum.ADD, description = "发送邮件任务信息")
    @PostMapping("send")
    @PreAuthorize("hasAuthority('sys:mailTasks:send')")
    public Result<?> send(@Valid @RequestBody SysMailTasksUpdateParams sysMailTasksUpdateParams) {
        sysMailTasksService.send(sysMailTasksUpdateParams.getSysMailTasksId());
        return new Result<>().ok();
    }

    @Operation(summary = "修改")
    @Log(module = "邮件任务-修改", type = OperationTypeEnum.UPDATE, description = "修改邮件任务信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:mailTasks:update')")
    public Result<?> update(@Valid @RequestBody SysMailTasksUpdateParams sysMailTasksUpdateParams) {
        sysMailTasksService.update(sysMailTasksUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:mailTasks:info')")
    public Result<SysMailTasksVO> get(@PathVariable("id") Long id) {
        SysMailTasksVO sysMailTasksVO = sysMailTasksService.getById(id);
        return new Result<SysMailTasksVO>().ok(sysMailTasksVO);
    }

    @Operation(summary = "删除")
    @Log(module = "邮件任务-删除", type = OperationTypeEnum.DELETE, description = "删除邮件任务信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('sys:mailTasks:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysMailTasksService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除")
    @Log(module = "邮件任务-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除邮件任务信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:mailTasks:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysMailTasksService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出")
    @Log(module = "邮件任务-导出", type = OperationTypeEnum.EXPORT, description = "导出邮件任务信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:mailTasks:export')")
    public void export(HttpServletResponse response, SysMailTasksSelectQueryParams sysMailTasksSelectQueryParams) throws IOException {
        List<SysMailTasksVO> list = sysMailTasksService.getList(sysMailTasksSelectQueryParams);
        List<SysMailTasksExcel> excelList = BeanUtil.copyToList(list, SysMailTasksExcel.class);
        ExcelUtils.exportExcel(response, "邮件任务.xlsx", SysMailTasksExcel.class, excelList);

    }
}

