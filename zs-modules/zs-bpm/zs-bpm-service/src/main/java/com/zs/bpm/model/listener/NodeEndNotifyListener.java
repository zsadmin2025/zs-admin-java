package com.zs.bpm.model.listener;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 节点结束通知监听器
 */
@Component("nodeEndNotifyListener")
public class NodeEndNotifyListener implements ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(NodeEndNotifyListener.class);

    @Override
    public void notify(DelegateExecution execution) {
        log.info("节点结束: activityId={}, activityName={}, processInstanceId={}", 
                execution.getCurrentActivityId(), 
                execution.getCurrentFlowElement() != null ? execution.getCurrentFlowElement().getName() : "unknown",
                execution.getProcessInstanceId());
    }
}