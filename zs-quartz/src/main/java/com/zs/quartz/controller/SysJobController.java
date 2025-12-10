package com.zs.quartz.controller;

import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import com.zs.quartz.domain.params.SysJobAddParams;
import com.zs.quartz.domain.params.SysJobQueryParams;
import com.zs.quartz.domain.params.SysJobUpdateParams;
import com.zs.quartz.domain.vo.SysJobVO;
import com.zs.quartz.service.ISysJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 定时任务
 */
@RestController
@RequestMapping("system/sys/job")
@Tag(name = "定时任务")
public class SysJobController {

    @Resource
    private  ISysJobService isSysJobService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:job:page')")
    public Result<PageResult<SysJobVO>> page(SysJobQueryParams sysJobQueryParams) {
        PageResult<SysJobVO> iPage = isSysJobService.page(sysJobQueryParams);
        return new Result<PageResult<SysJobVO>>().ok(iPage);
    }

    @Operation(summary = "新增定时任务")
    @Log(module = "定时任务-新增定时任务", type = OperationTypeEnum.ADD, description = "新增定时任务信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:job:save')")
    public Result<?> save(@RequestBody SysJobAddParams sysJobAddParams) {
        isSysJobService.save(sysJobAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改定时任务")
    @Log(module = "定时任务-修改定时任务", type = OperationTypeEnum.UPDATE, description = "修改定时任务信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:job:update')")
    public Result<?> update(@RequestBody SysJobUpdateParams sysJobUpdateParams) {
        isSysJobService.update(sysJobUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "查询定时任务详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('sys:job:info')")
    public Result<SysJobVO> get(@PathVariable("id") Long id) {
        SysJobVO sysJobVO = isSysJobService.get(id);
        return new Result<SysJobVO>().ok(sysJobVO);
    }

    @Operation(summary = "删除定时任务")
    @Log(module = "定时任务-删除定时任务", type = OperationTypeEnum.DELETE, description = "删除定时任务信息")
    @PostMapping("delete")
    @PreAuthorize("hasAuthority('sys:job:delete')")
    public Result<?> delete(@RequestBody SysJobUpdateParams sysJobUpdateParams) {
        isSysJobService.del(sysJobUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "暂停定时任务")
    @PostMapping("pause")
    @PreAuthorize("hasAuthority('sys:job:pause')")
    public Result<?> pause(@RequestBody SysJobUpdateParams sysJobUpdateParams) {
        isSysJobService.pause(sysJobUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "恢复定时任务")
    @PostMapping("resume")
    @PreAuthorize("hasAuthority('sys:job:resume')")
    public Result<?> resume(@RequestBody SysJobUpdateParams sysJobUpdateParams) {
        isSysJobService.resume(sysJobUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "立即执行定时任务")
    @PostMapping("run")
    @PreAuthorize("hasAuthority('sys:job:run')")
    public Result<?> run(@RequestBody SysJobUpdateParams sysJobUpdateParams) {
        isSysJobService.run(sysJobUpdateParams);
        return new Result<>().ok();
    }

}
