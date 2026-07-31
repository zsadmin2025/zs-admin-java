package com.zs.lawyer.cases.info.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.info.domain.excel.CaseInfoExcel;
import com.zs.lawyer.cases.info.domain.params.*;
import com.zs.lawyer.cases.info.domain.vo.CaseInfoVO;
import com.zs.lawyer.cases.info.domain.vo.CaseVO;
import com.zs.lawyer.cases.info.service.CaseInfoService;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApprovePageQueryParams;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件信息 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
@RestController
@RequestMapping("/lawyer/cases/info")
public class CaseInfoController {

    @Resource
    private CaseInfoService caseInfoService;

    /**
     * 分页查询案件信息
     * @param caseInfoPageQueryParams 案件信息分页查询参数
     * @return Result<PageResult<CaseVO>>
     */
    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:info:page')")
    public Result<PageResult<CaseVO>> page(CaseInfoPageQueryParams caseInfoPageQueryParams) {
        caseInfoPageQueryParams.setIsVoided(0);
        caseInfoPageQueryParams.setCaseStatusList(List.of());
        caseInfoPageQueryParams.setCaseStatus(null);
        PageResult<CaseVO> iPage = caseInfoService.page(caseInfoPageQueryParams);
        return new Result<PageResult<CaseVO>>().ok(iPage);
    }

    /**
     * 分页查询案件信息列表-跟进
     * @param caseInfoPageQueryParams 案件信息分页查询参数
     * @return Result<PageResult<CaseVO>>
     */
    @GetMapping("/followUp/page")
    @PreAuthorize("hasAuthority('cases:info:page')")
    public Result<PageResult<CaseVO>> followUpPage(CaseInfoPageQueryParams caseInfoPageQueryParams) {
        caseInfoPageQueryParams.setIsVoided(0);
        caseInfoPageQueryParams.setCaseStatusList(List.of());
        caseInfoPageQueryParams.setCaseStatus(1);
        PageResult<CaseVO> iPage = caseInfoService.page(caseInfoPageQueryParams);
        return new Result<PageResult<CaseVO>>().ok(iPage);
    }


    /**
     * 分页查询案件信息列表-跟进
     * @param caseInfoPageQueryParams 案件信息分页查询参数
     * @return Result<PageResult<CaseVO>>
     */
    @GetMapping("/closed/page")
    @PreAuthorize("hasAuthority('cases:info:page')")
    public Result<PageResult<CaseVO>> closedPage(CaseInfoPageQueryParams caseInfoPageQueryParams) {
        caseInfoPageQueryParams.setIsVoided(0);
        caseInfoPageQueryParams.setCaseStatusList(List.of());
        caseInfoPageQueryParams.setCaseStatus(1);
        PageResult<CaseVO> iPage = caseInfoService.page(caseInfoPageQueryParams);
        return new Result<PageResult<CaseVO>>().ok(iPage);
    }

    /**
     * 分页查询案件信息列表-归档
     * @param caseInfoPageQueryParams 案件信息分页查询参数
     * @return Result<PageResult<CaseVO>>
     */
    @GetMapping("/filing/page")
    @PreAuthorize("hasAuthority('cases:info:page')")
    public Result<PageResult<CaseVO>> filingPage(CaseInfoPageQueryParams caseInfoPageQueryParams) {
        caseInfoPageQueryParams.setIsVoided(0);
        caseInfoPageQueryParams.setCaseStatusList(List.of(2,3));
        PageResult<CaseVO> iPage = caseInfoService.page(caseInfoPageQueryParams);
        return new Result<PageResult<CaseVO>>().ok(iPage);
    }


    /**
     * 分页查询案件信息列表-作废
     * @param caseInfoPageQueryParams 案件信息分页查询参数
     * @return Result<PageResult<CaseVO>>
     */
    @GetMapping("/cancel/page")
    @PreAuthorize("hasAuthority('cases:info:page')")
    public Result<PageResult<CaseVO>> cancelPage(CaseInfoPageQueryParams caseInfoPageQueryParams) {
        caseInfoPageQueryParams.setIsVoided(1);
        caseInfoPageQueryParams.setCaseStatusList(List.of());
        caseInfoPageQueryParams.setCaseStatus(null);
        PageResult<CaseVO> iPage = caseInfoService.page(caseInfoPageQueryParams);
        return new Result<PageResult<CaseVO>>().ok(iPage);
    }


    /**
     * 分页查询案件信息列表-审批
     * @param caseInfoApprovePageQueryParams 审批案件信息分页查询参数
     * @return Result<PageResult<CaseVO>>
     */
    @GetMapping("/approve/page")
    @PreAuthorize("hasAuthority('cases:info:page')")
    public Result<PageResult<CaseVO>> approvePage(CaseInfoApprovePageQueryParams caseInfoApprovePageQueryParams) {
        PageResult<CaseVO> iPage = caseInfoService.approvePage(caseInfoApprovePageQueryParams);
        return new Result<PageResult<CaseVO>>().ok(iPage);
    }

    /**
     * 获取案件信息列表
     * @param caseInfoSelectQueryParams 案件信息查询参数
     * @return Result<List<CaseInfoVO>>
     */
    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:info:list')")
    public Result<List<CaseInfoVO>> list(CaseInfoSelectQueryParams caseInfoSelectQueryParams) {
        List<CaseInfoVO> list = caseInfoService.getList(caseInfoSelectQueryParams);
        return new Result<List<CaseInfoVO>>().ok(list);
    }

    @Log(module = "案件信息-审批通过", type = OperationTypeEnum.UPDATE, description = "通过审批案件信息")
    @PostMapping("approvePass")
    @PreAuthorize("hasAuthority('cases:info:approvePass')")
    public Result<?> passApprove(@RequestBody CaseApproveParams caseApproveParams) {
        caseInfoService.passApprove(caseApproveParams);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-审批否决", type = OperationTypeEnum.UPDATE, description = "否决审批案件信息")
    @PostMapping("approveReject")
    @PreAuthorize("hasAuthority('cases:info:approveReject')")
    public Result<?> approve(@RequestBody CaseApproveParams caseApproveParams) {
        caseInfoService.rejectApprove(caseApproveParams);
        return new Result<>().ok();
    }


    @Log(module = "案件信息-新增", type = OperationTypeEnum.ADD, description = "新增案件信息信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:info:save')")
    public Result<?> save(@RequestBody CaseAddParams caseAddParams) {
        caseInfoService.save(caseAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-修改", type = OperationTypeEnum.UPDATE, description = "修改案件信息信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:info:update')")
    public Result<?> update(@RequestBody CaseUpdateParams caseUpdateParams) {
        caseInfoService.update(caseUpdateParams);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-结案", type = OperationTypeEnum.UPDATE, description = "结案案件信息")
    @PostMapping("closed")
    @PreAuthorize("hasAuthority('cases:info:closed')")
    public Result<?> closed(@RequestBody CaseInfoStatusParams caseInfoStatusParams) {
        caseInfoService.closed(caseInfoStatusParams);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-归档", type = OperationTypeEnum.UPDATE, description = "归档案件信息")
    @PostMapping("filing")
    @PreAuthorize("hasAuthority('cases:info:filing')")
    public Result<?> filing(@RequestBody CaseInfoStatusParams caseInfoStatusParams) {
        caseInfoService.filing(caseInfoStatusParams);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-作废", type = OperationTypeEnum.UPDATE, description = "作废案件信息")
    @PostMapping("cancel")
    @PreAuthorize("hasAuthority('cases:info:cancel')")
    public Result<?> cancel(@RequestBody CaseInfoStatusParams caseInfoStatusParams) {
        caseInfoService.cancel(caseInfoStatusParams);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-恢复", type = OperationTypeEnum.UPDATE, description = "恢复案件信息")
    @PostMapping("restore")
    @PreAuthorize("hasAuthority('cases:info:restore')")
    public Result<?> restore(@RequestBody CaseInfoStatusParams caseInfoStatusParams) {
        caseInfoService.restore(caseInfoStatusParams);
        return new Result<>().ok();
    }

    @Log(module = "委托书-新增", type = OperationTypeEnum.ADD, description = "新增案件委托书")
    @PostMapping("/powerAttorney/save")
    @PreAuthorize("hasAuthority('cases:info:save')")
    public Result<?> savePowerAttorney(@RequestBody CaseInfoPowerAttorneyParams caseInfoPowerAttorneyParams) {
        caseInfoService.savePowerAttorney(caseInfoPowerAttorneyParams);
        return new Result<>().ok();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:info:info')")
    public Result<CaseVO> get(@PathVariable("id") Long id) {
        CaseVO caseVO = caseInfoService.getById(id);
        return new Result<CaseVO>().ok(caseVO);
    }


    @Log(module = "案件信息-删除", type = OperationTypeEnum.DELETE, description = "删除案件信息信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:info:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
        caseInfoService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件信息信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:info:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        caseInfoService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Log(module = "案件信息-导出", type = OperationTypeEnum.EXPORT, description = "导出案件信息信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:info:export')")
    public void export(HttpServletResponse response, CaseInfoSelectQueryParams caseInfoSelectQueryParams) throws IOException {
        List<CaseInfoVO> list = caseInfoService.getList(caseInfoSelectQueryParams);
        List<CaseInfoExcel> excelList = BeanUtil.copyToList(list, CaseInfoExcel.class);
        ExcelUtils.exportExcel(response, "案件信息.xlsx", CaseInfoExcel.class, excelList);

    }
}
