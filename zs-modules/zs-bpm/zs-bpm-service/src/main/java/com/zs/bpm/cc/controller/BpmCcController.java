package com.zs.bpm.cc.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zs.bpm.cc.domain.vo.BpmCcRecordVO;
import com.zs.bpm.cc.service.IBpmCcRecordService;
import com.zs.bpm.task.domain.params.TaskPageQueryParams;
import com.zs.bpm.task.service.IBpmTaskService;
import com.zs.common.aop.annotation.Log;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.OperationTypeEnum;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 抄送管理 Controller
 *
 * @author zsadmin
 */
@RestController
@RequestMapping("bpm/cc")
@Tag(name = "抄送管理")
public class BpmCcController {

    @Resource
    private IBpmTaskService bpmTaskService;

    @Resource
    private IBpmCcRecordService ccRecordService;

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "分页查询抄送我的列表")
    @GetMapping("page")
    public Result<PageResult<BpmCcRecordVO>> pageCc(TaskPageQueryParams params) {
        PageResult<BpmCcRecordVO> pageResult = bpmTaskService.ccPage(params);
        return new Result<PageResult<BpmCcRecordVO>>().ok(pageResult);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "抄送")
    @Log(module = "抄送管理-抄送", type = OperationTypeEnum.OTHER, description = "抄送")
    @PostMapping("send")
    public Result<?> sendCc(@RequestParam String processInstanceId,
                            @RequestParam(required = false) String taskId,
                            @RequestBody List<Long> userIds,
                            @RequestParam(required = false) String title) {
        bpmTaskService.sendCc(processInstanceId, taskId, userIds, title);
        return new Result<>().ok();
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "获取未读抄送数量")
    @GetMapping("unread-count")
    public Result<Long> getUnreadCount() {
        Long userId = SecurityUtil.getUserId();
        long count = ccRecordService.getUnreadCount(userId);
        return new Result<Long>().ok(count);
    }

    @ApiOperationSupport(author = "zs")
    @Operation(summary = "标记抄送为已读")
    @PutMapping("read/{id}")
    public Result<?> markAsRead(@PathVariable Long id) {
        ccRecordService.markAsRead(id);
        return new Result<>().ok();
    }
}
