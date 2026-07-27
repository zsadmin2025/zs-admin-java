package com.zs.bpm.model.listener;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 任务完成监听器
 */
@Component("taskCompleteListener")
public class TaskCompleteListener implements TaskListener {

    private static final Logger log = LoggerFactory.getLogger(TaskCompleteListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("任务完成: taskId={}, name={}, processInstanceId={}", 
                delegateTask.getId(), delegateTask.getName(), delegateTask.getProcessInstanceId());
    }
}