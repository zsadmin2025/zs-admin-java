package com.zs.sys.log.controller;


import cn.hutool.core.bean.BeanUtil;
import com.zs.common.core.core.Result;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.log.params.SysLogLoginAddParams;
import com.zs.common.core.page.PageResult;
import com.zs.sys.log.domain.excel.SysLogLoginExcel;
import com.zs.sys.log.domain.params.SysLogLoginQueryParams;
import com.zs.sys.log.domain.vo.SysLogLoginVO;
import com.zs.sys.log.service.ISysLogLoginService;
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
@RequestMapping("system/sys/log/login")
@Tag(name = "系统登录日志")
public class SysLogLoginController {


    @Resource
    private ISysLogLoginService iSysLogLoginService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:loglogin:page')")
    public Result<PageResult<SysLogLoginVO>> page(SysLogLoginQueryParams sysLogLoginQueryParams) {
        PageResult<SysLogLoginVO> iPage = iSysLogLoginService.page(sysLogLoginQueryParams);
        return new Result<PageResult<SysLogLoginVO>>().ok(iPage);
    }

    @Operation(summary = "今日登录日志")
    @GetMapping("todayList")
    public Result<List<SysLogLoginVO>> todayList() {
        List<SysLogLoginVO> list = iSysLogLoginService.todayList();
        return new Result<List<SysLogLoginVO>>().ok(list);
    }

    @Operation(summary = "新增登录日志")
    @PostMapping
    public Result<?> save(@RequestBody SysLogLoginAddParams sysLogLoginAddParams) {
        iSysLogLoginService.save(sysLogLoginAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "导出系统登录日志")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:loglogin:export')")
    public void export(HttpServletResponse response, SysLogLoginQueryParams sysLogLoginQueryParams) throws IOException {
        List<SysLogLoginVO> list = iSysLogLoginService.list(sysLogLoginQueryParams);
        List<SysLogLoginExcel> excelList = BeanUtil.copyToList(list, SysLogLoginExcel.class);
        ExcelUtils.exportExcel(response, "登录日志.xlsx", SysLogLoginExcel.class, excelList);
    }


}
