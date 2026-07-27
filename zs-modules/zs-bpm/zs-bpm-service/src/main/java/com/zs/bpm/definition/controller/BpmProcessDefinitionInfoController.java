package com.zs.bpm.definition.controller;

import com.zs.bpm.definition.domain.params.BpmProcessDefinitionInfoSaveParams;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.definition.service.IBpmProcessDefinitionInfoService;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoPageQueryParams;
import com.zs.common.core.core.Result;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程定义信息管理 Controller
 *
 * @author zsadmin
 */
@Tag(name = "流程定义信息管理")
@RestController
@RequestMapping("/bpm/setting/definition")
@RequiredArgsConstructor
public class BpmProcessDefinitionInfoController {

    private final IBpmProcessDefinitionInfoService definitionInfoService;

    @Operation(summary = "查询流程定义")
    @GetMapping("/queryProcessDefinition")
    public Result<List<ProcessDefinition>> queryProcessDefinition() {
        return new Result<List<ProcessDefinition>>().ok(definitionInfoService.queryProcessDefinition());
    }

    @Operation(summary = "分页查询流程定义")
//    @PreAuthorize("hasAuthority('bpm:definition:page')")
    @GetMapping("/page")
    public Result<PageResult<BpmProcessDefinitionInfoVO>> page(BpmProcessDefinitionInfoPageQueryParams params) {
        return new Result<PageResult<BpmProcessDefinitionInfoVO>>().ok(definitionInfoService.page(params));
    }

    @Operation(summary = "获取流程定义详情")
//    @PreAuthorize("hasAuthority('bpm:definition:page')")
    @GetMapping("/{id}")
    public Result<BpmProcessDefinitionInfoVO> get(@PathVariable Long id) {
        return new Result<BpmProcessDefinitionInfoVO>().ok(definitionInfoService.getDetail(id));
    }

    @Operation(summary = "保存流程定义")
//    @PreAuthorize("hasAuthority('bpm:definition:save')")
    @PostMapping("/save")
    public Result<Long> save(@RequestBody BpmProcessDefinitionInfoSaveParams params) {
        return new Result<Long>().ok(definitionInfoService.saveDefinition(params));
    }

}
