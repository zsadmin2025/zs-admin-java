package com.zs.sys.log.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.core.core.Result;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.log.params.SysLogErrorAddParams;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.excel.SysLogErrorExcel;
import com.zs.sys.log.domain.params.SysLogErrorQueryParams;
import com.zs.sys.log.domain.vo.SysLogErrorVO;
import com.zs.sys.log.service.ISysLogErrorService;
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
@RequestMapping("system/sys/log/error")
@Tag(name = "系统异常日志")
public class SysLogErrorController {


    @Resource
    private ISysLogErrorService iSysLogErrorService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:logerror:page')")
    public Result<PageResult<SysLogErrorVO>> page(SysLogErrorQueryParams sysLogErrorQueryParams) {
        PageResult<SysLogErrorVO> iPage = iSysLogErrorService.page(sysLogErrorQueryParams);
        return new Result<PageResult<SysLogErrorVO>>().ok(iPage);
    }

    @Operation(summary = "新增异常日志")
    @PostMapping
    public Result<?> save(@RequestBody SysLogErrorAddParams sysLogErrorAddParams) {
        iSysLogErrorService.save(sysLogErrorAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "导出异常日志")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:logerror:export')")
    public void export(HttpServletResponse response, SysLogErrorQueryParams sysLogErrorQueryParams) throws IOException {
        List<SysLogErrorVO> list = iSysLogErrorService.list(sysLogErrorQueryParams);
        List<SysLogErrorExcel> excelList = BeanUtil.copyToList(list, SysLogErrorExcel.class);
        ExcelUtils.exportExcel(response, "异常日志.xlsx", SysLogErrorExcel.class, excelList);

    }

}
