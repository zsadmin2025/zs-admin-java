package com.zs.infra.quartz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.core.core.Result;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.infra.quartz.domain.excel.SysJobLogExcel;
import com.zs.infra.quartz.domain.params.SysJobLogQueryParams;
import com.zs.infra.quartz.domain.vo.SysJobLogVO;
import com.zs.infra.quartz.service.ISysJobLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 */
@RestController
@RequestMapping("infra/job/log")
@Tag(name = "定时任务日志")
public class SysJobLogController {

    @Resource
    private ISysJobLogService sysJobLogService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:jobLog:page')")
    public Result<PageResult<SysJobLogVO>> page(SysJobLogQueryParams sysJobLogQueryParams) {
        PageResult<SysJobLogVO> iPage = sysJobLogService.page(sysJobLogQueryParams);
        return new Result<PageResult<SysJobLogVO>>().ok(iPage);
    }

    @Operation(summary = "导出")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('sys:jobLog:export')")
    public void export(HttpServletResponse response, SysJobLogQueryParams sysJobLogQueryParams) throws IOException {
        List<SysJobLogVO> list = sysJobLogService.list(sysJobLogQueryParams);
        List<SysJobLogExcel> excelList = BeanUtil.copyToList(list, SysJobLogExcel.class);
        ExcelUtils.exportExcel(response, "执行日志.xlsx", SysJobLogExcel.class, excelList);

    }
}
