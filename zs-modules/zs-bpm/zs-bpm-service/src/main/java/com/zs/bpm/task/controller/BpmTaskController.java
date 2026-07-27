package com.zs.bpm.task.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.bpm.task.domain.params.*;
import com.zs.bpm.task.domain.vo.AllTaskVO;
import com.zs.bpm.task.domain.vo.ProcessDetailVO;
import com.zs.bpm.task.domain.vo.ProcessInstanceInfo;
import com.zs.bpm.task.service.IBpmTaskService;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.flowable.task.api.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 任务管理 Controller
 *
 * @author zsadmin
 */
@RestController
@RequestMapping("bpm/task")
@Tag(name = "任务管理")
public class BpmTaskController {

    @Resource
    private IBpmTaskService bpmTaskService;

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "启动流程")
    @Log(module = "任务管理-启动", type = OperationTypeEnum.OTHER, description = "启动流程")
    @PostMapping("start")
    public Result<?> start(@RequestBody TaskProcessParams params) {
        bpmTaskService.startProcess(params);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询待办任务")
    @GetMapping("todo")
    public Result<PageResult<ProcessInstanceInfo>> pageTodo(TaskPageQueryParams params) {
        PageResult<ProcessInstanceInfo> pageResult = bpmTaskService.todoPage(params);
        return new Result<PageResult<ProcessInstanceInfo>>().ok(pageResult);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询已办任务")
    @GetMapping("done")
    public Result<PageResult<ProcessInstanceInfo>> finishedPage(TaskPageQueryParams params) {
        PageResult<ProcessInstanceInfo> pageResult = bpmTaskService.finishedPage(params);
        return new Result<PageResult<ProcessInstanceInfo>>().ok(pageResult);
    }





    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询所有运行中的任务")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("/task/running")
    public Result<PageResult<Task>> pageAllActiveTasks(TaskPageQueryParams params) {
        PageResult<Task> pageResult = bpmTaskService.getAllActiveTasks(params);
        return new Result<PageResult<Task>>().ok(pageResult);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询全部任务（待办+已办），用于流程任务菜单")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("allPage")
    public Result<PageResult<AllTaskVO>> pageAllTasks(AllTaskPageQueryParams params) {
        PageResult<AllTaskVO> pageResult = bpmTaskService.allTaskPage(params);
        return new Result<PageResult<AllTaskVO>>().ok(pageResult);
    }




    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取流程详情（审批全景）")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("detail")
    public Result<ProcessDetailVO> getProcessDetail(TodoTaskParams params) {
        ProcessDetailVO detail = bpmTaskService.getProcessDetail(params);
        return new Result<ProcessDetailVO>().ok(detail);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "根据业务主键查询流程详情")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("byBusinessKey/{businessKey}")
    public Result<ProcessDetailVO> getByBusinessKey(@PathVariable("businessKey") String businessKey) {
        ProcessDetailVO detail = bpmTaskService.getProcessDetailByBusinessKey(businessKey);
        return new Result<ProcessDetailVO>().ok(detail);
    }


    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取流程图")
//    @PreAuthorize("hasAuthority('bpm:task:query')")
    @GetMapping("diagram/{processInstanceId}")
    public Result<?> getProcessDiagram(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response) {
        bpmTaskService.getProcessDiagram(processInstanceId, response);
        return new Result<>().ok();
    }



    @ApiOperationSupport(author = "zs")
    @Operation(summary = "完成任务")
//    @PreAuthorize("hasAuthority('bpm:task:complete')")
    @Log(module = "任务管理-完成", type = OperationTypeEnum.OTHER, description = "完成任务")
    @PostMapping("complete")
    public Result<?> complete(@RequestBody TaskCompleteParams params) {
        bpmTaskService.complete(params);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "转办任务")
    @PreAuthorize("hasAuthority('bpm:task:transfer')")
    @Log(module = "任务管理-转办", type = OperationTypeEnum.OTHER, description = "转办任务")
    @PostMapping("transfer/{taskId}")
    public Result<?> transfer(@PathVariable("taskId") String taskId,
                              @RequestParam Long toUserId,
                              @RequestParam(required = false) String comment) {
        bpmTaskService.transfer(taskId, String.valueOf(toUserId));
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "委派任务")
    @PreAuthorize("hasAuthority('bpm:task:delegate')")
    @Log(module = "任务管理-委派", type = OperationTypeEnum.OTHER, description = "委派任务")
    @PostMapping("delegate/{taskId}")
    public Result<?> delegate(@PathVariable("taskId") String taskId,
                              @RequestParam Long toUserId,
                              @RequestParam(required = false) String comment) {
        bpmTaskService.delegateTask(taskId, String.valueOf(toUserId));
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "委派归还")
    @PreAuthorize("hasAuthority('bpm:task:delegate')")
    @Log(module = "任务管理-委派归还", type = OperationTypeEnum.OTHER, description = "委派归还")
    @PostMapping("resolve/{taskId}")
    public Result<?> resolve(@PathVariable("taskId") String taskId,
                             @RequestBody(required = false) Map<String, Object> variables) {
        bpmTaskService.resolve(taskId);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "加签")
    @PreAuthorize("hasAuthority('bpm:task:complete')")
    @Log(module = "任务管理-加签", type = OperationTypeEnum.OTHER, description = "加签")
    @PostMapping("addSign/{taskId}")
    public Result<?> addSign(@PathVariable("taskId") String taskId,
                             @RequestBody List<Long> userIds,
                             @RequestParam(defaultValue = "after") String position) {
        bpmTaskService.addSign(taskId, userIds.stream().map(String::valueOf).collect(java.util.stream.Collectors.toList()), "after".equals(position));
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "减签")
    @PreAuthorize("hasAuthority('bpm:task:complete')")
    @Log(module = "任务管理-减签", type = OperationTypeEnum.OTHER, description = "减签")
    @PostMapping("removeSign/{taskId}")
    public Result<?> removeSign(@PathVariable("taskId") String taskId,
                                @RequestBody List<Long> userIds) {
        bpmTaskService.removeSign(taskId, String.valueOf(userIds.get(0)));
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "撤销流程")
//    @PreAuthorize("hasAuthority('bpm:task:complete')")
    @Log(module = "任务管理-撤销", type = OperationTypeEnum.OTHER, description = "撤销流程")
    @PostMapping("cancel/{processInstanceId}")
    public Result<?> cancel(@PathVariable("processInstanceId") String processInstanceId,
                            @RequestParam(required = false) String reason) {
        bpmTaskService.cancel(processInstanceId, reason);
        return new Result<>().ok();
    }
}
