package com.zs.sys.notice.controller;


import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import com.zs.sys.notice.domain.params.SysNoticeAddParams;
import com.zs.sys.notice.domain.params.SysNoticeQueryParams;
import com.zs.sys.notice.domain.params.SysNoticeUpdateParams;
import com.zs.sys.notice.domain.vo.SysNoticeVO;
import com.zs.sys.notice.service.SysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zsadmin
 */
@RestController
@RequestMapping("system/sys/notice")
@Tag(name = "通知公告")
public class SysNoticeController {

    @Resource
    private SysNoticeService sysNoticeService;


    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('sys:notice:page')")
    public Result<PageResult<SysNoticeVO>> page(SysNoticeQueryParams sysNoticeQueryParams) {
        PageResult<SysNoticeVO> page = sysNoticeService.page(sysNoticeQueryParams);
        return new Result<PageResult<SysNoticeVO>>().ok(page);
    }


    @Operation(summary = "新增通知公告草稿")
    @Log(module = "通知公告-新增草稿", type = OperationTypeEnum.ADD, description = "新增通知公告草稿信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:notice:save')")
    public Result<?> save(@RequestBody  SysNoticeAddParams sysNoticeAddParams) {
        sysNoticeService.save(sysNoticeAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改通知公告草稿")
    @Log(module = "通知公告-草稿修改", type = OperationTypeEnum.UPDATE, description = "修改通知公告草稿信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('sys:notice:update')")
    public Result<?> update(@RequestBody SysNoticeUpdateParams sysNoticeUpdateParams) {
        sysNoticeService.update(sysNoticeUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "删除通知公告")
    @Log(module = "通知公告-删除", type = OperationTypeEnum.DELETE, description = "删除通知公告信息")
    @DeleteMapping("{sysNoticeId}")
    @PreAuthorize("hasAuthority('sys:notice:delete')")
    public Result<?> delete(@PathVariable Long sysNoticeId) {
        sysNoticeService.delete(sysNoticeId);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除通知公告")
    @Log(module = "通知公告-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除通知公告信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:notice:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
        sysNoticeService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "通知公告详情")
    @GetMapping("{sysNoticeId}")
    @PreAuthorize("hasAuthority('sys:notice:info')")
    public Result<SysNoticeVO> get(@PathVariable Long sysNoticeId) {
        SysNoticeVO sysNoticeVO = sysNoticeService.get(sysNoticeId);
        return new Result<SysNoticeVO>().ok(sysNoticeVO);
    }

    @Operation(summary = "立即发布通知公告")
    @Log(module = "通知公告-立即发布", type = OperationTypeEnum.UPDATE, description = "立即发布通知公告信息")
    @PostMapping("releaseImmediately")
    @PreAuthorize("hasAuthority('sys:notice:releaseImmediately')")
    public Result<?> releaseImmediately(@RequestBody SysNoticeAddParams sysNoticeAddParams) {
        sysNoticeService.releaseImmediately(sysNoticeAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "发布通知公告")
    @Log(module = "通知公告-发布", type = OperationTypeEnum.UPDATE, description = "发布通知公告信息")
    @PostMapping("release")
    @PreAuthorize("hasAuthority('sys:notice:release')")
    public Result<?> release(@RequestBody SysNoticeUpdateParams sysNoticeUpdateParams) {
        sysNoticeService.release(sysNoticeUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "撤销通知公告")
    @Log(module = "通知公告-撤销", type = OperationTypeEnum.UPDATE, description = "撤销通知公告信息")
    @PostMapping("revoke")
    @PreAuthorize("hasAuthority('sys:notice:revoke')")
    public Result<?> revoke(@RequestBody SysNoticeUpdateParams sysNoticeUpdateParams) {
        sysNoticeService.revoke(sysNoticeUpdateParams);
        return new Result<>().ok();
    }


    @Operation(summary = "获取指定数量通知公告")
    @GetMapping("/limit/{num}")
//    @PreAuthorize("hasAuthority('sys:notice:info')")
    public Result<List<SysNoticeVO>>  getLimit(@PathVariable Integer num) {
        List<SysNoticeVO> sysNoticeVOList = sysNoticeService.getLimit(num);
        return new Result<List<SysNoticeVO>>().ok(sysNoticeVOList);
    }

}
