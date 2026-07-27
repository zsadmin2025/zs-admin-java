package com.zs.bpm.model.listener;

import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ccNotificationListener")
public class CcNotificationListener implements ExecutionListener {

    private static final String NS = "http://flowable.org/bpmn";

//    @Resource
//    private OrgUserService orgUserService;
//    @Resource
//    private MessageService messageService;

    @Override
    public void notify(DelegateExecution execution) {
        if (!(execution.getCurrentFlowElement() instanceof ServiceTask task)) return;

        String param = getExtText(task, "candidateParam");
        List<String> ccUsers = List.of(param.split(","));
//        ccUsers.forEach(userId ->
//                messageService.sendCcNotice(userId, execution.getProcessInstanceId(), task.getName())
//        );
    }

    private String getExtText(ServiceTask task, String name) {
        List<ExtensionElement> list = task.getExtensionElements().get(NS + name);
        return list == null || list.isEmpty() ? "" : list.get(0).getElementText();
    }
}
