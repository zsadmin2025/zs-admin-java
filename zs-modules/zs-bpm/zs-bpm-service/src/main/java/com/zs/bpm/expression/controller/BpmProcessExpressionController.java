package com.zs.bpm.expression.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.bpm.expression.domain.entity.BpmProcessExpressionEntity;
import com.zs.bpm.expression.domain.params.BpmProcessExpressionQueryParams;
import com.zs.bpm.expression.domain.vo.BpmProcessExpressionVO;
import com.zs.bpm.expression.service.IBpmProcessExpressionService;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 流程表达式 Controller
 *
 * @author zsadmin
 */
@RestController
@RequestMapping("bpm/setting/expression")
@Tag(name = "流程表达式")
public class BpmProcessExpressionController {

    @Resource
    private IBpmProcessExpressionService bpmProcessExpressionService;

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询表达式")
//    @PreAuthorize("hasAuthority('bpm:expression:page')")
    @GetMapping("page")
    public Result<PageResult<BpmProcessExpressionVO>> page(BpmProcessExpressionQueryParams queryParams) {
        PageResult<BpmProcessExpressionVO> page = bpmProcessExpressionService.pageQuery(queryParams);
        return new Result<PageResult<BpmProcessExpressionVO>>().ok(page);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "新增表达式")
    @Log(module = "流程表达式-新增", type = OperationTypeEnum.ADD, description = "新增流程表达式")
//    @PreAuthorize("hasAuthority('bpm:expression:save')")
    @PostMapping("save")
    public Result<?> save(@RequestBody BpmProcessExpressionEntity entity) {
        bpmProcessExpressionService.save(entity);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "修改表达式")
    @Log(module = "流程表达式-修改", type = OperationTypeEnum.UPDATE, description = "修改流程表达式")
//    @PreAuthorize("hasAuthority('bpm:expression:save')")
    @PutMapping("update")
    public Result<?> update(@RequestBody BpmProcessExpressionEntity entity) {
        bpmProcessExpressionService.updateById(entity);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "删除表达式")
    @Log(module = "流程表达式-删除", type = OperationTypeEnum.DELETE, description = "删除流程表达式")
//    @PreAuthorize("hasAuthority('bpm:expression:delete')")
    @DeleteMapping("{id}")
    public Result<?> delete(@PathVariable("id") Long id) {
        bpmProcessExpressionService.deleteById(id);
        return new Result<>().ok();
    }


    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取表达式")
    @GetMapping("{id}")
    public Result<BpmProcessExpressionVO> get(@PathVariable("id") Long id) {
        BpmProcessExpressionVO entity = bpmProcessExpressionService.getById(id);
        return new Result<BpmProcessExpressionVO>().ok(entity);
    }

}
