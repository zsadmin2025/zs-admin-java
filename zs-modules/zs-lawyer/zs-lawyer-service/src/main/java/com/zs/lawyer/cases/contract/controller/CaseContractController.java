package com.zs.lawyer.cases.contract.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.contract.domain.excel.CaseContractExcel;
import com.zs.lawyer.cases.contract.domain.params.CaseContractAddParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractPageQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractSelectQueryParams;
import com.zs.lawyer.cases.contract.domain.params.CaseContractUpdateParams;
import com.zs.lawyer.cases.contract.domain.vo.CaseContractVO;
import com.zs.lawyer.cases.contract.service.CaseContractService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 案件合同 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:02:46
 */
@RestController
@RequestMapping("/lawyer/cases/contract")
public class CaseContractController {

    @Resource
    private CaseContractService caseContractService;

    @GetMapping("page")
    @PreAuthorize("hasAuthority('cases:contract:page')")
    public Result<PageResult<CaseContractVO>> page(CaseContractPageQueryParams caseContractPageQueryParams) {
        PageResult<CaseContractVO> iPage = caseContractService.page(caseContractPageQueryParams);
        return new Result<PageResult<CaseContractVO>>().ok(iPage);
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('cases:contract:list')")
    public Result<List<CaseContractVO>> list(CaseContractSelectQueryParams caseContractSelectQueryParams) {
        List<CaseContractVO> list = caseContractService.getList(caseContractSelectQueryParams);
        return new Result<List<CaseContractVO>>().ok(list);
    }

    @Log(module = "案件合同-新增", type = OperationTypeEnum.ADD, description = "新增案件合同信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('cases:contract:save')")
    public Result<?> save(@RequestBody CaseContractAddParams caseContractAddParams) {
				caseContractService.save(caseContractAddParams);
        return new Result<>().ok();
    }

    @Log(module = "案件合同-修改", type = OperationTypeEnum.UPDATE, description = "修改案件合同信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('cases:contract:update')")
    public Result<?> update(@RequestBody CaseContractUpdateParams caseContractUpdateParams) {
				caseContractService.update(caseContractUpdateParams);
        return new Result<>().ok();
    }


    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('cases:contract:info')")
    public Result<CaseContractVO> get(@PathVariable("id") Long id) {
				CaseContractVO caseContractVO = caseContractService.getById(id);
        return new Result<CaseContractVO>().ok(caseContractVO);
    }


    @Log(module = "案件合同-删除", type = OperationTypeEnum.DELETE, description = "删除案件合同信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('cases:contract:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				caseContractService.deleteById(id);
        return new Result<>().ok();
    }

    @Log(module = "案件合同-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除案件合同信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('cases:contract:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				caseContractService.batchDelById(ids);
        return new Result<>().ok();
    }
    @Log(module = "案件合同-导出", type = OperationTypeEnum.EXPORT, description = "导出案件合同信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('cases:contract:export')")
    public void export(HttpServletResponse response, CaseContractSelectQueryParams caseContractSelectQueryParams) throws IOException {
        List<CaseContractVO> list = caseContractService.getList(caseContractSelectQueryParams);
        List<CaseContractExcel> excelList = BeanUtil.copyToList(list, CaseContractExcel.class);
        ExcelUtils.exportExcel(response, "案件合同.xlsx", CaseContractExcel.class, excelList);

    }
}
