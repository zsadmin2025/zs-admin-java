package com.zs.lawyer.cases.infoApprovalForm.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoApprovalForm.domain.excel.CaseInfoApprovalFormExcel;
import com.zs.lawyer.cases.infoApprovalForm.domain.params.*;
import com.zs.lawyer.cases.infoApprovalForm.domain.vo.CaseInfoApprovalFormVO;
import com.zs.lawyer.cases.infoApprovalForm.service.CaseInfoApprovalFormService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件审批表 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@RestController
@RequestMapping("/lawyer/cases/infoApprovalForm")
public class CaseInfoApprovalFormController {

    @Resource
    private CaseInfoApprovalFormService caseInfoApprovalFormService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:page')")
    public Result<PageResult<CaseInfoApprovalFormVO>> page(CaseInfoApprovalFormPageQueryParams caseInfoApprovalFormPageQueryParams) {
        PageResult<CaseInfoApprovalFormVO> iPage = caseInfoApprovalFormService.page(caseInfoApprovalFormPageQueryParams);
        return new Result<PageResult<CaseInfoApprovalFormVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:list')")
    public Result<List<CaseInfoApprovalFormVO>> list(CaseInfoApprovalFormSelectQueryParams caseInfoApprovalFormSelectQueryParams) {
        List<CaseInfoApprovalFormVO> list = caseInfoApprovalFormService.getList(caseInfoApprovalFormSelectQueryParams);
        return new Result<List<CaseInfoApprovalFormVO>>().ok(list);
    }

    @Log(module = "案件审批表-新增", type = OperationTypeEnum.ADD, description = "新增案件审批表信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:save')")
    public Result<?> save(@RequestBody CaseInfoApprovalFormAddParams caseInfoApprovalFormAddParams) {
        caseInfoApprovalFormService.save(caseInfoApprovalFormAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件审批表-修改", type = OperationTypeEnum.UPDATE, description = "修改案件审批表信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:update')")
    public Result<?> update(@RequestBody CaseInfoApprovalFormUpdateParams caseInfoApprovalFormUpdateParams) {
        caseInfoApprovalFormService.update(caseInfoApprovalFormUpdateParams);
        return new Result<>().ok();
    }

    @Log(module = "案件审批表-提交签批", type = OperationTypeEnum.ADD, description = "提交签批案件审批表信息")
    @PostMapping("submitApprovalForm")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:save')")
    public Result<?> submitApprovalForm(@RequestBody CaseInfoApprovalFormUpdateParams caseInfoApprovalFormUpdateParams) {
        caseInfoApprovalFormService.submitApprovalForm(caseInfoApprovalFormUpdateParams);
        return new Result<>().ok();
    }

    @Log(module = "案件审批表-通过审批", type = OperationTypeEnum.ADD, description = "审批案件审批表信息")
    @PostMapping("passApprove")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:approval')")
    public Result<?> passApprove(@RequestBody CaseInfoApprovalFormParams caseInfoApprovalFormParams) {
        caseInfoApprovalFormService.passApprove(caseInfoApprovalFormParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:info')")
    public Result<CaseInfoApprovalFormVO> get(@PathVariable("id") Long id) {
        CaseInfoApprovalFormVO caseInfoApprovalFormVO = caseInfoApprovalFormService.getById(id);
        return new Result<CaseInfoApprovalFormVO>().ok(caseInfoApprovalFormVO);
    }


    @Log(module = "案件审批表-删除", type = OperationTypeEnum.DELETE, description = "删除案件审批表信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        caseInfoApprovalFormService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件审批表-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件审批表信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        caseInfoApprovalFormService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Log(module = "案件审批表-导出", type = OperationTypeEnum.EXPORT, description = "导出案件审批表信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:infoApprovalForm:export')")
    public void export(HttpServletResponse response, CaseInfoApprovalFormSelectQueryParams caseInfoApprovalFormSelectQueryParams) throws IOException {
        List<CaseInfoApprovalFormVO> list = caseInfoApprovalFormService.getList(caseInfoApprovalFormSelectQueryParams);
        List<CaseInfoApprovalFormExcel> excelList = BeanUtil.copyToList(list, CaseInfoApprovalFormExcel.class);
        ExcelUtils.exportExcel(response, "案件审批表.xlsx", CaseInfoApprovalFormExcel.class, excelList);

    }
}
