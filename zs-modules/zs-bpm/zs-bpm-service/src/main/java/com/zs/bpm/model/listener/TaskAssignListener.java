package com.zs.bpm.model.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 任务分配监听器
 * <p>
 * 利用 Flowable 多实例的 {@code elementVariable="approver"} 直接获取审批人，
 * 不再通过 {@code assigneeList + loopCounter} 迂回取值。
 * </p>
 */
@Component("taskAssignListener")
public class TaskAssignListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(TaskAssignListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        // 用 WARN 级别确保日志可见
        log.warn("=== TaskAssignListener 触发: taskId={}, name={} ===",
                delegateTask.getId(), delegateTask.getName());

        // 方式1：读 approver
        String approver = (String) delegateTask.getVariable("approver");
        log.warn("=== 方式1 approver: {} ===", approver);
        if (StrUtil.isNotBlank(approver)) {
            delegateTask.setAssignee(approver);
            log.warn("=== setAssignee(approver) 完成: {} ===", approver);
            return;
        }

        // 方式2：读 assigneeList + loopCounter
        @SuppressWarnings("unchecked")
        List<String> userList = (List<String>) delegateTask.getVariable("assigneeList");
        Integer lc = (Integer) delegateTask.getVariable("loopCounter");
        log.warn("=== 方式2 assigneeList: {}, loopCounter: {} ===", userList, lc);
        if (CollUtil.isNotEmpty(userList)) {
            String assignee = lc != null && lc < userList.size() ? userList.get(lc) : userList.get(0);
            delegateTask.setAssignee(assignee);
            log.warn("=== setAssignee(list) 完成: {} ===", assignee);
            return;
        }

        // 方式3：读 startUserId 兜底
        String startUserId = (String) delegateTask.getVariable("startUserId");
        log.warn("=== 方式3 startUserId: {} ===", startUserId);
        if (StrUtil.isNotBlank(startUserId)) {
            delegateTask.setAssignee(startUserId);
            log.warn("=== setAssignee(startUserId) 完成: {} ===", startUserId);
            return;
        }

        log.error("=== setAssignee 全部失败: 三个变量均为 null ===");
    }
}