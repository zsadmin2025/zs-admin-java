package com.zs.sys.message.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.excel.ExcelUtils;
import com.zs.common.core.page.PageResult;
import com.zs.sys.message.domain.excel.SysMessagesExcel;
import com.zs.sys.message.domain.params.SysMessagesAddParams;
import com.zs.sys.message.domain.params.SysMessagesPageQueryParams;
import com.zs.sys.message.domain.params.SysMessagesSelectQueryParams;
import com.zs.sys.message.domain.params.SysMessagesUpdateParams;
import com.zs.sys.message.domain.vo.SysMessagesVO;
import com.zs.sys.message.service.SysMessagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 * 消息 前端控制器
 * </p>
 *
 * @author zs
 * @since 2025-11-17 09:01:44
 */
@RestController
@RequestMapping("/system/sys/messages")
@Tag(name = "消息管理")
public class SysMessagesController {

    @Resource
    private SysMessagesService sysMessagesService;

    @Operation(summary = "分页查询")
    @GetMapping("page")
    @PreAuthorize("hasAuthority('system:messages:page')")
    public Result<PageResult<SysMessagesVO>> page(SysMessagesPageQueryParams sysMessagesPageQueryParams) {
        PageResult<SysMessagesVO> iPage = sysMessagesService.page(sysMessagesPageQueryParams);
        return new Result<PageResult<SysMessagesVO>>().ok(iPage);
    }

    @Operation(summary = "消息列表")
    @GetMapping("list")
    public Result<List<SysMessagesVO>> list(SysMessagesSelectQueryParams sysMessagesSelectQueryParams) {
        List<SysMessagesVO> list = sysMessagesService.getList(sysMessagesSelectQueryParams);
        return new Result<List<SysMessagesVO>>().ok(list);
    }

    @Operation(summary = "新增消息")
    @Log(module = "消息-新增", type = OperationTypeEnum.ADD, description = "新增消息信息")
    @PostMapping("save")
    @PreAuthorize("hasAuthority('system:messages:save')")
    public Result<?> save(@RequestBody SysMessagesAddParams sysMessagesAddParams) {
				sysMessagesService.save(sysMessagesAddParams);
        return new Result<>().ok();
    }

    @Operation(summary = "修改消息")
    @Log(module = "消息-修改", type = OperationTypeEnum.UPDATE, description = "修改消息信息")
    @PutMapping("update")
    @PreAuthorize("hasAuthority('system:messages:update')")
    public Result<?> update(@RequestBody SysMessagesUpdateParams sysMessagesUpdateParams) {
				sysMessagesService.update(sysMessagesUpdateParams);
        return new Result<>().ok();
    }

    @Operation(summary = "消息已读")
    @Log(module = "消息-已读", type = OperationTypeEnum.UPDATE, description = "已读消息信息")
    @PutMapping("update/read")
    @PreAuthorize("hasAuthority('system:messages:update')")
    public Result<?> batchUpdateRead(@RequestBody Long[] ids) {
        sysMessagesService.batchUpdateRead(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "消息详情")
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('system:messages:info')")
    public Result<SysMessagesVO> get(@PathVariable("id") Long id) {
				SysMessagesVO sysMessagesVO = sysMessagesService.getById(id);
        return new Result<SysMessagesVO>().ok(sysMessagesVO);
    }

    @Operation(summary = "删除消息")
    @Log(module = "消息-删除", type = OperationTypeEnum.DELETE, description = "删除消息信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('system:messages:delete')")
    public Result<?> delete(@PathVariable("id") Long id) {
				sysMessagesService.deleteById(id);
        return new Result<>().ok();
    }

    @Operation(summary = "批量删除消息")
    @Log(module = "消息-批量删除", type = OperationTypeEnum.DELETE_BATCH, description = "批量删除消息信息")
    @DeleteMapping
    @PreAuthorize("hasAuthority('system:messages:batchDelete')")
    public Result<?> batchDelete(@RequestBody Long[] ids) {
				sysMessagesService.batchDelById(ids);
        return new Result<>().ok();
    }

    @Operation(summary = "导出消息")
    @Log(module = "消息-导出", type = OperationTypeEnum.EXPORT, description = "导出消息信息")
    @GetMapping("export")
    @PreAuthorize("hasAuthority('system:messages:export')")
    public void export(HttpServletResponse response, SysMessagesSelectQueryParams sysMessagesSelectQueryParams) throws IOException {
        List<SysMessagesVO> list = sysMessagesService.getList(sysMessagesSelectQueryParams);
        List<SysMessagesExcel> excelList = BeanUtil.copyToList(list, SysMessagesExcel.class);
        ExcelUtils.exportExcel(response, "消息.xlsx", SysMessagesExcel.class, excelList);

    }
}
