package com.zs.sms.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sms.domain.entity.SysSmsRecordEntity;
import com.zs.sms.domain.excel.SysSmsTemplateExcel;
import com.zs.sms.domain.params.*;
import com.zs.sms.domain.vo.SysSmsTemplateVO;
import com.zs.sms.factory.SmsFactory;
import com.zs.sms.service.SysSmsRecordService;
import com.zs.sms.service.SysSmsTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 短信模板 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-11-26 09:40:35
 */
@RestController
@RequestMapping("/system/sms/template")
@Tag(name = "短信模板")
public class SysSmsTemplateController {

    @Resource
    private SysSmsTemplateService sysSmsTemplateService;
    @Resource
    private SysSmsRecordService sysSmsRecordService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('system:smsTemplate:page')")
    public Result<PageResult<SysSmsTemplateVO>> page(SysSmsTemplatePageQueryParams sysSmsTemplatePageQueryParams) {
        PageResult<SysSmsTemplateVO> iPage = sysSmsTemplateService.page(sysSmsTemplatePageQueryParams);
        return new Result<PageResult<SysSmsTemplateVO>>().ok(iPage);
    }

    @Operation(summary = "列表查询")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('system:smsTemplate:list')")
    public Result<List<SysSmsTemplateVO>> list(SysSmsTemplateSelectQueryParams sysSmsTemplateSelectQueryParams) {
        List<SysSmsTemplateVO> list = sysSmsTemplateService.getList(sysSmsTemplateSelectQueryParams);
        return new Result<List<SysSmsTemplateVO>>().ok(list);
    }

    @Operation(summary = "新增")
    @Log(module = "短信模板-新增", type = OperationTypeEnum.ADD, description = "新增短信模板信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:smsTemplate:save')")
    public Result<?> save(@RequestBody SysSmsTemplateAddParams sysSmsTemplateAddParams) {
        sysSmsTemplateService.save(sysSmsTemplateAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改")
    @Log(module = "短信模板-修改", type = OperationTypeEnum.UPDATE, description = "修改短信模板信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('system:smsTemplate:update')")
    public Result<?> update(@RequestBody SysSmsTemplateUpdateParams sysSmsTemplateUpdateParams) {
        sysSmsTemplateService.update(sysSmsTemplateUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('system:smsTemplate:info')")
    public Result<SysSmsTemplateVO> get(@PathVariable("id") Long id) {
        SysSmsTemplateVO sysSmsTemplateVO = sysSmsTemplateService.getById(id);
        return new Result<SysSmsTemplateVO>().ok(sysSmsTemplateVO);
    }


    @Operation(summary = "删除")
    @Log(module = "短信模板-删除", type = OperationTypeEnum.DELETE, description = "删除短信模板信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('system:smsTemplate:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        sysSmsTemplateService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除")
    @Log(module = "短信模板-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除短信模板信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('system:smsTemplate:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysSmsTemplateService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出")
    @Log(module = "短信模板-导出", type = OperationTypeEnum.EXPORT, description = "导出短信模板信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('system:smsTemplate:export')")
    public void export(HttpServletResponse response, SysSmsTemplateSelectQueryParams sysSmsTemplateSelectQueryParams) throws IOException {
        List<SysSmsTemplateVO> list = sysSmsTemplateService.getList(sysSmsTemplateSelectQueryParams);
        List<SysSmsTemplateExcel> excelList = BeanUtil.copyToList(list, SysSmsTemplateExcel.class);
        ExcelUtils.exportExcel(response, "短信模板.xlsx", SysSmsTemplateExcel.class, excelList);

    }



    @Operation(summary = "发送短信")
    @Log(module = "短信记录-发送", type = OperationTypeEnum.ADD, description = "发送短信")
    @PostMapping("send")
    @PreAuthorize("hasAuthority('system:smsTemplate:send')")
    public Result<?> send(@Valid @RequestBody SysSmsParams sysSmsParams) {

        try {
            //根据不同的策略选择不同的发送方式
            SysSmsRecordEntity sysSmsRecordEntity = Objects.requireNonNull(SmsFactory.build()).send(sysSmsTemplateService, sysSmsParams);
            // 保存到数据库
            // 保存发送记录
            sysSmsRecordService.save(sysSmsRecordEntity);

            if (sysSmsRecordEntity == null) {
                return new Result<>(500, "发送失败");
            }
            return new Result<>(200, "发送成功", sysSmsRecordEntity);
        }catch (Exception e) {
            return new Result<>(500, "发送失败");
        }
    }
}
