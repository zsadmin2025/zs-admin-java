package com.zs.lawyer.cases.infoFiles.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoFiles.domain.excel.CaseInfoFilesExcel;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesAddParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesPageQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesSelectQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesUpdateParams;
import com.zs.lawyer.cases.infoFiles.domain.vo.CaseInfoFilesVO;
import com.zs.lawyer.cases.infoFiles.service.CaseInfoFilesService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件附件 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-21 09:34:39
 */
@RestController
@RequestMapping("/lawyer/cases/infoFiles")
public class CaseInfoFilesController {

    @Resource
    private CaseInfoFilesService caseInfoFilesService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:infoFiles:page')")
    public Result<PageResult<CaseInfoFilesVO>> page(CaseInfoFilesPageQueryParams caseInfoFilesPageQueryParams) {
        PageResult<CaseInfoFilesVO> iPage = caseInfoFilesService.page(caseInfoFilesPageQueryParams);
        return new Result<PageResult<CaseInfoFilesVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:infoFiles:list')")
    public Result<List<CaseInfoFilesVO>> list(CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams) {
        List<CaseInfoFilesVO> list = caseInfoFilesService.getList(caseInfoFilesSelectQueryParams);
        return new Result<List<CaseInfoFilesVO>>().ok(list);
    }

    @Log(module = "案件附件-新增", type = OperationTypeEnum.ADD, description = "新增案件附件信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:infoFiles:save')")
    public Result<?> save(@RequestBody CaseInfoFilesAddParams caseInfoFilesAddParams) {
				caseInfoFilesService.save(caseInfoFilesAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件附件-修改", type = OperationTypeEnum.UPDATE, description = "修改案件附件信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:infoFiles:update')")
    public Result<?> update(@RequestBody CaseInfoFilesUpdateParams caseInfoFilesUpdateParams) {
				caseInfoFilesService.update(caseInfoFilesUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoFiles:info')")
    public Result<CaseInfoFilesVO> get(@PathVariable("id") Long id) {
				CaseInfoFilesVO caseInfoFilesVO = caseInfoFilesService.getById(id);
        return new Result<CaseInfoFilesVO>().ok(caseInfoFilesVO);
    }


    @Log(module = "案件附件-删除", type = OperationTypeEnum.DELETE, description = "删除案件附件信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoFiles:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				caseInfoFilesService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件附件-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件附件信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:infoFiles:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				caseInfoFilesService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "案件附件-导出", type = OperationTypeEnum.EXPORT, description = "导出案件附件信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:infoFiles:export')")
    public void export(HttpServletResponse response, CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams) throws IOException {
        List<CaseInfoFilesVO> list = caseInfoFilesService.getList(caseInfoFilesSelectQueryParams);
        List<CaseInfoFilesExcel> excelList = BeanUtil.copyToList(list, CaseInfoFilesExcel.class);
        ExcelUtils.exportExcel(response, "案件附件.xlsx", CaseInfoFilesExcel.class, excelList);

    }
}
