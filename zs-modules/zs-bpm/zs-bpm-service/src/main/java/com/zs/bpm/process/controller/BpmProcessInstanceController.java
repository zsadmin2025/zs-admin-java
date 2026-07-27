package com.zs.bpm.process.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.bpm.process.domain.vo.ProcessInstanceVO;
import com.zs.bpm.process.service.IBpmProcessInstanceService;
import com.zs.bpm.task.domain.params.TaskPageQueryParams;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 流程实例管理 Controller
 *
 * @author zsadmin
 */
@Tag(name = "流程实例管理")
@RestController
@RequestMapping("/bpm/setting/instance")
@RequiredArgsConstructor
public class BpmProcessInstanceController {

    private final IBpmProcessInstanceService bpmProcessInstanceService;

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询所有流程实例（含审批轨迹）")
//    @PreAuthorize("hasAuthority('bpm:process:instance:page')")
    @GetMapping("/all/page")
    public Result<PageResult<ProcessInstanceVO>> allPage(TaskPageQueryParams params) {
        PageResult<ProcessInstanceVO> pageResult = bpmProcessInstanceService.getAllProcessInstance(params);
        return new Result<PageResult<ProcessInstanceVO>>().ok(pageResult);
    }


    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询我的流程")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("/my/processes")
    public Result<PageResult<ProcessInstanceVO>> pageMyProcesses(TaskPageQueryParams params) {
        PageResult<ProcessInstanceVO> pageResult = bpmProcessInstanceService.myProcesses(params);
        return new Result<PageResult<ProcessInstanceVO>>().ok(pageResult);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询运行中的实例")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("/running/page")
    public Result<PageResult<ProcessInstance>> pageRunningProcessInstance(TaskPageQueryParams params) {
        PageResult<ProcessInstance> pageResult = bpmProcessInstanceService.getRunningProcessInstance(params);
        return new Result<PageResult<ProcessInstance>>().ok(pageResult);
    }



    @ApiOperationSupport(author = "zs")
    @Operation(summary = "挂起流程实例")
    // @PreAuthorize("hasAuthority('bpm:process:instance:suspend')")
    @Log(module = "流程实例-挂起", type = OperationTypeEnum.OTHER, description = "挂起流程实例")
    @PostMapping("/suspend/{processInstanceId}")
    public Result<?> suspend(@PathVariable("processInstanceId") String processInstanceId) {
        bpmProcessInstanceService.suspend(processInstanceId);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "激活流程实例")
    // @PreAuthorize("hasAuthority('bpm:process:instance:activate')")
    @Log(module = "流程实例-激活", type = OperationTypeEnum.OTHER, description = "激活流程实例")
    @PostMapping("/activate/{processInstanceId}")
    public Result<?> activate(@PathVariable("processInstanceId") String processInstanceId) {
        bpmProcessInstanceService.activate(processInstanceId);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "终止流程实例")
    // @PreAuthorize("hasAuthority('bpm:process:instance:terminate')")
    @Log(module = "流程实例-终止", type = OperationTypeEnum.OTHER, description = "终止流程实例")
    @PostMapping("/terminate/{processInstanceId}")
    public Result<?> terminate(@PathVariable("processInstanceId") String processInstanceId,
                               @RequestParam(required = false) String reason) {
        bpmProcessInstanceService.terminate(processInstanceId, reason);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取流程图追踪")
    @PreAuthorize("hasAuthority('bpm:process:instance:trace')")
    @GetMapping("/trace/{processInstanceId}")
    public Result<Map<String, Object>> getTrace(@PathVariable("processInstanceId") String processInstanceId) {
        Map<String, Object> trace = bpmProcessInstanceService.getTrace(processInstanceId);
        return new Result<Map<String, Object>>().ok(trace);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取流程变量")
    @PreAuthorize("hasAuthority('bpm:process:instance:variables')")
    @GetMapping("/variables/{processInstanceId}")
    public Result<Map<String, Object>> getVariables(@PathVariable("processInstanceId") String processInstanceId) {
        Map<String, Object> variables = bpmProcessInstanceService.getVariables(processInstanceId);
        return new Result<Map<String, Object>>().ok(variables);
    }
}
