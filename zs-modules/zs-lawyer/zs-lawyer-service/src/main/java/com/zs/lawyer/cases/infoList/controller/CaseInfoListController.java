package com.zs.lawyer.cases.infoList.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoList.domain.excel.CaseInfoListExcel;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListAddParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListPageQueryParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListSelectQueryParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListUpdateParams;
import com.zs.lawyer.cases.infoList.domain.vo.CaseInfoListVO;
import com.zs.lawyer.cases.infoList.service.CaseInfoListService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件结案目录 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-21 12:20:27
 */
@RestController
@RequestMapping("/lawyer/cases/infoList")
public class CaseInfoListController {

    @Resource
    private CaseInfoListService caseInfoListService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:infoList:page')")
    public Result<PageResult<CaseInfoListVO>> page(CaseInfoListPageQueryParams caseInfoListPageQueryParams) {
        PageResult<CaseInfoListVO> iPage = caseInfoListService.page(caseInfoListPageQueryParams);
        return new Result<PageResult<CaseInfoListVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:infoList:list')")
    public Result<List<CaseInfoListVO>> list(CaseInfoListSelectQueryParams caseInfoListSelectQueryParams) {
        List<CaseInfoListVO> list = caseInfoListService.getList(caseInfoListSelectQueryParams);
        return new Result<List<CaseInfoListVO>>().ok(list);
    }

    @Log(module = "案件结案目录-新增", type = OperationTypeEnum.ADD, description = "新增案件结案目录信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:infoList:save')")
    public Result<?> save(@RequestBody CaseInfoListAddParams caseInfoListAddParams) {
        caseInfoListService.save(caseInfoListAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件结案目录-修改", type = OperationTypeEnum.UPDATE, description = "修改案件结案目录信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:infoList:update')")
    public Result<?> update(@RequestBody CaseInfoListUpdateParams caseInfoListUpdateParams) {
        caseInfoListService.update(caseInfoListUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoList:info')")
    public Result<CaseInfoListVO> get(@PathVariable("id") Long id) {
        CaseInfoListVO caseInfoListVO = caseInfoListService.getById(id);
        return new Result<CaseInfoListVO>().ok(caseInfoListVO);
    }


    @Log(module = "案件结案目录-删除", type = OperationTypeEnum.DELETE, description = "删除案件结案目录信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:infoList:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        caseInfoListService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件结案目录-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件结案目录信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:infoList:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        caseInfoListService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Log(module = "案件结案目录-导出", type = OperationTypeEnum.EXPORT, description = "导出案件结案目录信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:infoList:export')")
    public void export(HttpServletResponse response, CaseInfoListSelectQueryParams caseInfoListSelectQueryParams) throws IOException {
        List<CaseInfoListVO> list = caseInfoListService.getList(caseInfoListSelectQueryParams);
        List<CaseInfoListExcel> excelList = BeanUtil.copyToList(list, CaseInfoListExcel.class);
        ExcelUtils.exportExcel(response, "案件结案目录.xlsx", CaseInfoListExcel.class, excelList);

    }
}
