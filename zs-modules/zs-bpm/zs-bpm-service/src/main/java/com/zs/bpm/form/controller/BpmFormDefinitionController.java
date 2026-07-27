package com.zs.bpm.form.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.bpm.form.domain.params.FormDefinitionAddParams;
import com.zs.bpm.form.domain.params.FormDefinitionQueryParams;
import com.zs.bpm.form.domain.vo.FormDefinitionVO;
import com.zs.bpm.form.service.IBpmFormDefinitionService;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 动态表单定义 Controller
 *
 * @author zsadmin
 */
@RestController
@RequestMapping("bpm/form/definition")
@Tag(name = "动态表单管理")
public class BpmFormDefinitionController {

    @Resource
    private IBpmFormDefinitionService bpmFormDefinitionService;

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询表单定义")
//    @PreAuthorize("hasAuthority('bpm:form:page')")
    @GetMapping("page")
    public Result<PageResult<FormDefinitionVO>> page(FormDefinitionQueryParams params) {
        PageResult<FormDefinitionVO> page = bpmFormDefinitionService.page(params);
        return new Result<PageResult<FormDefinitionVO>>().ok(page);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "新增表单定义")
    @Log(module = "动态表单-新增", type = OperationTypeEnum.ADD, description = "新增表单定义")
//    @PreAuthorize("hasAuthority('bpm:form:save')")
    @PostMapping("save")
    public Result<?> save(@RequestBody FormDefinitionAddParams params) {
        bpmFormDefinitionService.save(params);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "修改表单定义")
    @Log(module = "动态表单-修改", type = OperationTypeEnum.UPDATE, description = "修改表单定义")
//    @PreAuthorize("hasAuthority('bpm:form:save')")
    @PutMapping("update")
    public Result<?> update(@RequestBody FormDefinitionAddParams params) {
        bpmFormDefinitionService.update(params);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "删除表单定义")
    @Log(module = "动态表单-删除", type = OperationTypeEnum.DELETE, description = "删除表单定义")
//    @PreAuthorize("hasAuthority('bpm:form:delete')")
    @DeleteMapping("{id}")
    public Result<?> delete(@PathVariable("id") Long id) {
        bpmFormDefinitionService.delete(id);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取表单定义详情")
//    @PreAuthorize("hasAuthority('bpm:form:page')")
    @GetMapping("{id}")
    public Result<FormDefinitionVO> get(@PathVariable("id") Long id) {
        FormDefinitionVO vo = bpmFormDefinitionService.getById(id);
        return new Result<FormDefinitionVO>().ok(vo);
    }





}
