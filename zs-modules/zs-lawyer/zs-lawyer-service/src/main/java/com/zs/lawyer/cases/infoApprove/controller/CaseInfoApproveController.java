package com.zs.lawyer.cases.infoApprove.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoApprove.domain.excel.CaseInfoApproveExcel;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveAddParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApprovePageQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveSelectQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveUpdateParams;
import com.zs.lawyer.cases.infoApprove.domain.vo.CaseInfoApproveVO;
import com.zs.lawyer.cases.infoApprove.service.CaseInfoApproveService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件审批 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-30 09:04:42
 */
@RestController
@RequestMapping("/lawyer/cases/infoApprove")
public class CaseInfoApproveController {

    @Resource
    private CaseInfoApproveService caseInfoApproveService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:infoApprove:page')")
    public Result<PageResult<CaseInfoApproveVO>> page(CaseInfoApprovePageQueryParams caseInfoApprovePageQueryParams) {
        PageResult<CaseInfoApproveVO> iPage = caseInfoApproveService.page(caseInfoApprovePageQueryParams);
        return new Result<PageResult<CaseInfoApproveVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:infoApprove:list')")
    public Result<List<CaseInfoApproveVO>> list(CaseInfoApproveSelectQueryParams caseInfoApproveSelectQueryParams) {
        List<CaseInfoApproveVO> list = caseInfoApproveService.getList(caseInfoApproveSelectQueryParams);
        return new Result<List<CaseInfoApproveVO>>().ok(list);
    }

    @Log(module = "案件审批-新增", type = OperationTypeEnum.ADD, description = "新增案件审批信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:infoApprove:save')")
    public Result<?> save(@RequestBody CaseInfoApproveAddParams caseInfoApproveAddParams) {
				caseInfoApproveService.save(caseInfoApproveAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件审批-修改", type = OperationTypeEnum.UPDATE, description = "修改案件审批信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:infoApprove:update')")
    public Result<?> update(@RequestBody CaseInfoApproveUpdateParams caseInfoApproveUpdateParams) {
				caseInfoApproveService.update(caseInfoApproveUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoApprove:info')")
    public Result<CaseInfoApproveVO> get(@PathVariable("id") Long id) {
				CaseInfoApproveVO caseInfoApproveVO = caseInfoApproveService.getById(id);
        return new Result<CaseInfoApproveVO>().ok(caseInfoApproveVO);
    }


    @Log(module = "案件审批-删除", type = OperationTypeEnum.DELETE, description = "删除案件审批信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoApprove:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				caseInfoApproveService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件审批-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件审批信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:infoApprove:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				caseInfoApproveService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "案件审批-导出", type = OperationTypeEnum.EXPORT, description = "导出案件审批信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:infoApprove:export')")
    public void export(HttpServletResponse response, CaseInfoApproveSelectQueryParams caseInfoApproveSelectQueryParams) throws IOException {
        List<CaseInfoApproveVO> list = caseInfoApproveService.getList(caseInfoApproveSelectQueryParams);
        List<CaseInfoApproveExcel> excelList = BeanUtil.copyToList(list, CaseInfoApproveExcel.class);
        ExcelUtils.exportExcel(response, "案件审批.xlsx", CaseInfoApproveExcel.class, excelList);

    }
}
