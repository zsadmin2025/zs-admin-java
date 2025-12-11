package com.zs.sys.log.controller;


import cn.hutool.core.bean.BeanUtil;
import com.zs.common.core.core.Result;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.log.params.SysLogOperationAddParams;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.excel.SysLogOperationExcel;
import com.zs.sys.log.domain.params.SysLogOperationQueryParams;
import com.zs.sys.log.domain.vo.SysLogOperationVO;
import com.zs.sys.log.service.ISysLogOperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/log/operation")
@Tag(name = "系统操作日志")
public class SysLogOperationController {


    @Resource
    private ISysLogOperationService iSysLogOperationService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:logOperation:page')")
    public Result<PageResult<SysLogOperationVO>> page(SysLogOperationQueryParams sysLogOperationQueryParams) {
        PageResult<SysLogOperationVO> iPage = iSysLogOperationService.page(sysLogOperationQueryParams);
        return new Result<PageResult<SysLogOperationVO>>().ok(iPage);
    }

    @Operation(summary = "新增操作日志")
    @PostMapping
    public Result<?> save(@RequestBody SysLogOperationAddParams sysLogOperationAddParams) {
        iSysLogOperationService.save(sysLogOperationAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "导出操作日志")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:logOperation:export')")
    public void export(HttpServletResponse response, SysLogOperationQueryParams sysLogOperationQueryParams) throws IOException {
        List<SysLogOperationVO> list = iSysLogOperationService.list(sysLogOperationQueryParams);
        List<SysLogOperationExcel> excelList = BeanUtil.copyToList(list, SysLogOperationExcel.class);
        ExcelUtils.exportExcel(response, "操作日志.xlsx", SysLogOperationExcel.class, excelList);
    }
}
