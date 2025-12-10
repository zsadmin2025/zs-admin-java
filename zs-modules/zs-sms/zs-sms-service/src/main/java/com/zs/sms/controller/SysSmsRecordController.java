package com.zs.sms.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sms.domain.excel.SysSmsRecordExcel;
import com.zs.sms.domain.params.SysSmsRecordPageQueryParams;
import com.zs.sms.domain.params.SysSmsRecordSelectQueryParams;
import com.zs.sms.domain.params.SysSmsRecordUpdateParams;
import com.zs.sms.domain.vo.SysSmsRecordVO;
import com.zs.sms.service.SysSmsRecordService;
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
 * 短信记录 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-11-25 22:20:34
 */
@RestController
@RequestMapping("/system/sms/record")
@Tag(name = "短信记录")
public class SysSmsRecordController {

    @Resource
    private SysSmsRecordService sysSmsService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('system:smsRecord:page')")
    public Result<PageResult<SysSmsRecordVO>> page(SysSmsRecordPageQueryParams sysSmsPageQueryParams) {
        PageResult<SysSmsRecordVO> iPage = sysSmsService.page(sysSmsPageQueryParams);
        return new Result<PageResult<SysSmsRecordVO>>().ok(iPage);
    }

    @Operation(summary = "查询列表")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('system:smsRecord:list')")
    public Result<List<SysSmsRecordVO>> list(SysSmsRecordSelectQueryParams sysSmsSelectQueryParams) {
        List<SysSmsRecordVO> list = sysSmsService.getList(sysSmsSelectQueryParams);
        return new Result<List<SysSmsRecordVO>>().ok(list);
    }


    @Operation(summary = "修改")
    @Log(module = "短信记录-修改", type = OperationTypeEnum.UPDATE, description = "修改短信记录信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('system:smsRecord:update')")
    public Result<?> update(@Valid @RequestBody SysSmsRecordUpdateParams sysSmsUpdateParams) {
        sysSmsService.update(sysSmsUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('system:smsRecord:info')")
    public Result<SysSmsRecordVO> get(@PathVariable("id") Long id) {
        SysSmsRecordVO sysSmsVO = sysSmsService.getById(id);
        return new Result<SysSmsRecordVO>().ok(sysSmsVO);
    }


    @Operation(summary = "删除")
    @Log(module = "短信记录-删除", type = OperationTypeEnum.DELETE, description = "删除短信记录信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('system:smsRecord:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysSmsService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除")
    @Log(module = "短信记录-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除短信记录信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('system:smsRecord:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysSmsService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出")
    @Log(module = "短信记录-导出", type = OperationTypeEnum.EXPORT, description = "导出短信记录信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('system:smsRecord:export')")
    public void export(HttpServletResponse response, SysSmsRecordSelectQueryParams sysSmsSelectQueryParams) throws IOException {
        List<SysSmsRecordVO> list = sysSmsService.getList(sysSmsSelectQueryParams);
        List<SysSmsRecordExcel> excelList = BeanUtil.copyToList(list, SysSmsRecordExcel.class);
        ExcelUtils.exportExcel(response, "短信记录.xlsx", SysSmsRecordExcel.class, excelList);

    }
}
