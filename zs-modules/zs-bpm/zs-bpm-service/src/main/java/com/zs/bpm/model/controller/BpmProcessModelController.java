package com.zs.bpm.model.controller;

import com.zs.bpm.definition.domain.params.BpmProcessDefinitionInfoSaveParams;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoPageQueryParams;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoQueryParams;
import com.zs.bpm.model.domain.vo.ModelValidateVO;
import com.zs.bpm.model.service.IBpmProcessModelService;
import com.zs.common.core.core.Result;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "流程模型管理") 
@RestController
@RequestMapping("/bpm/model")
@RequiredArgsConstructor
public class BpmProcessModelController {

    private final IBpmProcessModelService processModelService;

    @Operation(summary = "分页查询模型列表")
//    @PreAuthorize("hasAuthority('bpm:model:page')")
    @GetMapping("/page")
    public Result<PageResult<?>> page(BpmProcessDefinitionInfoPageQueryParams params) {
        return new Result<PageResult<?>>().ok((PageResult<?>) processModelService.page(params));
    }

    @Operation(summary = "获取模型详情")
//    @PreAuthorize("hasAuthority('bpm:definition:page')")
    @GetMapping("/{id}")
    public Result<BpmProcessDefinitionInfoVO> get(@PathVariable Long id) {
        return new Result<BpmProcessDefinitionInfoVO>().ok(processModelService.getDetail(id));
    }

    @Operation(summary = "获取可发起流程的流程列表")
    @GetMapping("/canStartList")
    public Result<List<BpmProcessDefinitionInfoVO>> getCanStartProcessList(BpmProcessDefinitionInfoQueryParams params) {
        return new Result<List<BpmProcessDefinitionInfoVO>>().ok(processModelService.getCanStartProcessList(params));
    }

    @Operation(summary = "创建流程模型")
//    @PreAuthorize("hasAuthority('bpm:model:create')")
    @PostMapping("/create")
    public Result<Long> createModel(@Valid @RequestBody BpmProcessDefinitionInfoSaveParams params) {
        return new Result<Long>().ok(processModelService.createModel(params));
    }

    @Operation(summary = "更新流程模型")
//    @PreAuthorize("hasAuthority('bpm:model:update')")
    @PutMapping("/update")
    public Result<Long> updateModel(@Valid @RequestBody BpmProcessDefinitionInfoSaveParams params) {
        return new Result<Long>().ok(processModelService.updateModel(params));
    }

    @Operation(summary = "部署模型")
//    @PreAuthorize("hasAuthority('bpm:model:deploy')")
    @PostMapping("/deploy/{id}")
    public Result<Boolean> deployModel(@PathVariable("id") Long id) {
        return new Result<Boolean>().ok(processModelService.deployModel(id));
    }








    @Operation(summary = "校验BPMN模型")
//    @PreAuthorize("hasAuthority('bpm:model:validate')")
    @GetMapping("/validate/{id}")
    public Result<ModelValidateVO> validateModel(@PathVariable Long id) {
        return new Result<ModelValidateVO>().ok(processModelService.validateModel(id));
    }

//     @Operation(summary = "发布模型")
// //    @PreAuthorize("hasAuthority('bpm:model:publish')")
//     @PostMapping("/publish/{id}")
//     public Result<Long> publishModel(@PathVariable Long id) {
//         return new Result<Long>().ok(processModelService.publishModel(id));
//     }



    @Operation(summary = "启用流程定义")
//    @PreAuthorize("hasAuthority('bpm:model:activate')")
    @PostMapping("/activate/{defId}")
    public Result<String> activateProcess(@PathVariable String defId) {
        return new Result<String>().ok(processModelService.activateProcess(defId));
    }

    @Operation(summary = "停用流程定义")
//    @PreAuthorize("hasAuthority('bpm:model:deactivate')")
    @PostMapping("/deactivate/{defId}")
    public Result<String> deactivateProcess(@PathVariable String defId) {
        return new Result<String>().ok(processModelService.deactivateProcess(defId));
    }


}
