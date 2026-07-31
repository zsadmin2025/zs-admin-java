package com.zs.lawyer.cases.hearing.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.hearing.domain.excel.CaseHearingExcel;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingAddParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingPageQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingSelectQueryParams;
import com.zs.lawyer.cases.hearing.domain.params.CaseHearingUpdateParams;
import com.zs.lawyer.cases.hearing.domain.vo.CaseHearingVO;
import com.zs.lawyer.cases.hearing.service.CaseHearingService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件开庭信息 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
@RestController
@RequestMapping("/lawyer/cases/hearing")
public class CaseHearingController {

    @Resource
    private CaseHearingService caseHearingService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:hearing:page')")
    public Result<PageResult<CaseHearingVO>> page(CaseHearingPageQueryParams caseHearingPageQueryParams) {
        PageResult<CaseHearingVO> iPage = caseHearingService.page(caseHearingPageQueryParams);
        return new Result<PageResult<CaseHearingVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:hearing:list')")
    public Result<List<CaseHearingVO>> list(CaseHearingSelectQueryParams caseHearingSelectQueryParams) {
        List<CaseHearingVO> list = caseHearingService.getList(caseHearingSelectQueryParams);
        return new Result<List<CaseHearingVO>>().ok(list);
    }

    @Log(module = "案件开庭信息-新增", type = OperationTypeEnum.ADD, description = "新增案件开庭信息信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:hearing:save')")
    public Result<?> save(@RequestBody CaseHearingAddParams caseHearingAddParams) {
				caseHearingService.save(caseHearingAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件开庭信息-修改", type = OperationTypeEnum.UPDATE, description = "修改案件开庭信息信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:hearing:update')")
    public Result<?> update(@RequestBody CaseHearingUpdateParams caseHearingUpdateParams) {
				caseHearingService.update(caseHearingUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:hearing:info')")
    public Result<CaseHearingVO> get(@PathVariable("id") Long id) {
				CaseHearingVO caseHearingVO = caseHearingService.getById(id);
        return new Result<CaseHearingVO>().ok(caseHearingVO);
    }


    @Log(module = "案件开庭信息-删除", type = OperationTypeEnum.DELETE, description = "删除案件开庭信息信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:hearing:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				caseHearingService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件开庭信息-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件开庭信息信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:hearing:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				caseHearingService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "案件开庭信息-导出", type = OperationTypeEnum.EXPORT, description = "导出案件开庭信息信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:hearing:export')")
    public void export(HttpServletResponse response, CaseHearingSelectQueryParams caseHearingSelectQueryParams) throws IOException {
        List<CaseHearingVO> list = caseHearingService.getList(caseHearingSelectQueryParams);
        List<CaseHearingExcel> excelList = BeanUtil.copyToList(list, CaseHearingExcel.class);
        ExcelUtils.exportExcel(response, "案件开庭信息.xlsx", CaseHearingExcel.class, excelList);

    }
}
