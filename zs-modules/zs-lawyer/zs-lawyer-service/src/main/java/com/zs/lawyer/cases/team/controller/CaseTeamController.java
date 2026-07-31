package com.zs.lawyer.cases.team.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.team.domain.excel.CaseTeamExcel;
import com.zs.lawyer.cases.team.domain.params.CaseTeamAddParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamPageQueryParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamSelectQueryParams;
import com.zs.lawyer.cases.team.domain.params.CaseTeamUpdateParams;
import com.zs.lawyer.cases.team.domain.vo.CaseTeamVO;
import com.zs.lawyer.cases.team.service.CaseTeamService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件团队 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:01:20
 */
@RestController
@RequestMapping("/lawyer/cases/team")
public class CaseTeamController {

    @Resource
    private CaseTeamService caseTeamService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:team:page')")
    public Result<PageResult<CaseTeamVO>> page(CaseTeamPageQueryParams caseTeamPageQueryParams) {
        PageResult<CaseTeamVO> iPage = caseTeamService.page(caseTeamPageQueryParams);
        return new Result<PageResult<CaseTeamVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:team:list')")
    public Result<List<CaseTeamVO>> list(CaseTeamSelectQueryParams caseTeamSelectQueryParams) {
        List<CaseTeamVO> list = caseTeamService.getList(caseTeamSelectQueryParams);
        return new Result<List<CaseTeamVO>>().ok(list);
    }

    @Log(module = "案件团队-新增", type = OperationTypeEnum.ADD, description = "新增案件团队信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:team:save')")
    public Result<?> save(@RequestBody CaseTeamAddParams caseTeamAddParams) {
				caseTeamService.save(caseTeamAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件团队-修改", type = OperationTypeEnum.UPDATE, description = "修改案件团队信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:team:update')")
    public Result<?> update(@RequestBody CaseTeamUpdateParams caseTeamUpdateParams) {
				caseTeamService.update(caseTeamUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:team:info')")
    public Result<CaseTeamVO> get(@PathVariable("id") Long id) {
				CaseTeamVO caseTeamVO = caseTeamService.getById(id);
        return new Result<CaseTeamVO>().ok(caseTeamVO);
    }


    @Log(module = "案件团队-删除", type = OperationTypeEnum.DELETE, description = "删除案件团队信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:team:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				caseTeamService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件团队-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件团队信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:team:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				caseTeamService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "案件团队-导出", type = OperationTypeEnum.EXPORT, description = "导出案件团队信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:team:export')")
    public void export(HttpServletResponse response, CaseTeamSelectQueryParams caseTeamSelectQueryParams) throws IOException {
        List<CaseTeamVO> list = caseTeamService.getList(caseTeamSelectQueryParams);
        List<CaseTeamExcel> excelList = BeanUtil.copyToList(list, CaseTeamExcel.class);
        ExcelUtils.exportExcel(response, "案件团队.xlsx", CaseTeamExcel.class, excelList);

    }
}
