package com.zs.bpm.model.delegate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zs.bpm.cc.domain.entity.BpmCcRecordEntity;
import com.zs.bpm.cc.service.IBpmCcRecordService;
import com.zs.bpm.model.resolver.ApproverResolver;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.HistoricActivityInstanceQueryImpl;
import org.flowable.engine.impl.persistence.entity.ActivityInstanceEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.flowable.engine.impl.persistence.entity.HistoricActivityInstanceEntityManager;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抄送任务委托实现
 * <p>
 * 当流程执行到抄送 ServiceTask 时，从扩展元素 nodeConfig 中按 settype 解析抄送人，
 * 写入抄送记录，并回写当前活动实例的 ASSIGNEE_（ServiceTask 默认不会写该字段）。
 * </p>
 *
 * @author zsadmin
 */
@Slf4j
@Component("bpmCopyTaskDelegate")
public class BpmCopyTaskDelegate implements JavaDelegate {

    private static final int DEFAULT_RESOLVER_CODE = -1;

    @Resource
    private IBpmCcRecordService ccRecordService;

    private final Map<Integer, ApproverResolver> resolverMap = new HashMap<>();

    @Autowired
    public void setResolvers(List<ApproverResolver> resolvers) {
        resolvers.forEach(r -> resolverMap.put(r.getResolverCode(), r));
    }

    @Override
    public void execute(DelegateExecution execution) {
        if (!(execution.getCurrentFlowElement() instanceof ServiceTask serviceTask)) {
            log.warn("当前节点不是 ServiceTask，跳过抄送处理");
            return;
        }

        JSONObject nodeConfig = getNodeConfig(serviceTask);
        if (nodeConfig == null) {
            log.warn("抄送节点缺少 nodeConfig 配置，跳过: taskId={}", serviceTask.getId());
            return;
        }

        Integer strategy = nodeConfig.getInt("settype");
        String param = getCandidateParam(nodeConfig);
        String startUserId = execution.getVariable("startUserId", String.class);

        ApproverResolver resolver = resolverMap.getOrDefault(strategy, resolverMap.get(DEFAULT_RESOLVER_CODE));
        if (resolver == null) {
            log.error("抄送节点无对应解析器: strategy={}, taskId={}", strategy, serviceTask.getId());
            return;
        }

        List<String> ccUsers = resolver.resolve(param, startUserId);
        if (CollUtil.isEmpty(ccUsers)) {
            log.warn("抄送节点未解析到抄送人: strategy={}, param={}, taskId={}",
                    strategy, param, serviceTask.getId());
            return;
        }

        String processInstanceId = execution.getProcessInstanceId();
        String activityId = execution.getCurrentActivityId();
        String taskName = serviceTask.getName();

        // 自动抄送的发起人为流程发起人
        Long ccSenderId = null;
        if (StrUtil.isNotBlank(startUserId)) {
            try {
                ccSenderId = Long.parseLong(startUserId);
            } catch (NumberFormatException e) {
                log.warn("流程发起人ID格式异常: {}", startUserId);
            }
        }

        log.info("执行抄送: processInstanceId={}, activityId={}, ccUsers={}, ccSenderId={}",
                processInstanceId, activityId, ccUsers, ccSenderId);

        for (String userIdStr : ccUsers) {
            if (StrUtil.isBlank(userIdStr)) {
                continue;
            }
            try {
                Long userId = Long.parseLong(userIdStr.trim());
                BpmCcRecordEntity record = new BpmCcRecordEntity();
                record.setProcessInstanceId(processInstanceId);
                record.setTaskId(activityId);
                record.setUserId(userId);
                record.setTitle(taskName);
                record.setIsRead(0);
                record.setCcSenderId(ccSenderId);
                record.setCcType(1);  // 流程自动抄送
                ccRecordService.save(record);
            } catch (NumberFormatException e) {
                log.warn("抄送用户 ID 格式异常: {}", userIdStr);
            }
        }

        // ServiceTask 引擎不会自动写 ASSIGNEE_，需在活动实例上手动回写
        writeActivityAssignee(execution, ccUsers);

        log.info("抄送处理完成: processInstanceId={}, 抄送人数={}", processInstanceId, ccUsers.size());
    }

    /**
     * 将抄送人回写到 act_ru_actinst / act_hi_actinst 的 ASSIGNEE_。
     * 多人时用逗号拼接（该字段本为单人设计，业务抄送人以 bpm_cc_record 为准）。
     */
    private void writeActivityAssignee(DelegateExecution execution, List<String> ccUsers) {
        String assignee = String.join(",", ccUsers);
        String activityId = execution.getCurrentActivityId();
        String processInstanceId = execution.getProcessInstanceId();
        String executionId = execution.getId();

        boolean runtimeWritten = false;
        boolean historyWritten = false;

        // 1. 写入运行时活动实例（act_ru_actinst）
        if (execution instanceof ExecutionEntity executionEntity) {
            try {
                ActivityInstanceEntity activityInstance = CommandContextUtil
                        .getActivityInstanceEntityManager()
                        .findUnfinishedActivityInstance(executionEntity);
                if (activityInstance != null) {
                    activityInstance.setAssignee(assignee);
                    runtimeWritten = true;
                } else {
                    log.warn("未找到运行时活动实例: executionId={}, activityId={}",
                            executionId, activityId);
                }
            } catch (Exception e) {
                log.warn("写入运行时活动实例 ASSIGNEE_ 失败: executionId={}, activityId={}",
                        executionId, activityId, e);
            }
        } else {
            log.warn("execution 不是 ExecutionEntity，跳过运行时 ASSIGNEE_ 写入: type={}, executionId={}",
                    execution.getClass().getName(), executionId);
        }

        // 2. 写入历史活动实例（act_hi_actinst）
        try {
            HistoricActivityInstanceEntityManager historicManager =
                    CommandContextUtil.getHistoricActivityInstanceEntityManager();
            List<HistoricActivityInstanceEntity> historicList;

            // 方式1：按 executionId + activityId 查找（原逻辑）
            historicList = historicManager
                    .findUnfinishedHistoricActivityInstancesByExecutionAndActivityId(
                            executionId, activityId);

            // 方式2：若方式1未命中，回退到按 processInstanceId + activityId 查找
            if (CollUtil.isEmpty(historicList)) {
                HistoricActivityInstanceQueryImpl query = new HistoricActivityInstanceQueryImpl();
                query.processInstanceId(processInstanceId);
                query.activityId(activityId);
                query.unfinished();
                @SuppressWarnings("unchecked")
                List<HistoricActivityInstanceEntity> fallbackList =
                        (List<HistoricActivityInstanceEntity>) (List<?>)
                                historicManager.findHistoricActivityInstancesByQueryCriteria(query);
                historicList = fallbackList != null ? fallbackList : Collections.emptyList();
            }

            if (CollUtil.isNotEmpty(historicList)) {
                for (HistoricActivityInstanceEntity entity : historicList) {
                    entity.setAssignee(assignee);
                }
                historyWritten = true;
            } else {
                log.warn("未找到历史活动实例: processInstanceId={}, activityId={}",
                        processInstanceId, activityId);
            }
        } catch (Exception e) {
            log.warn("写入历史活动实例 ASSIGNEE_ 失败: processInstanceId={}, activityId={}",
                    processInstanceId, activityId, e);
        }

        log.info("抄送活动 ASSIGNEE_ 回写结果: activityId={}, assignee={}, runtimeWritten={}, historyWritten={}",
                activityId, assignee, runtimeWritten, historyWritten);
    }

    private JSONObject getNodeConfig(ServiceTask task) {
        String configJson = getExtensionText(task, "nodeConfig");
        if (StrUtil.isBlank(configJson)) {
            configJson = getExtensionText(task, "approveConfig");
        }
        if (StrUtil.isBlank(configJson)) {
            return null;
        }
        try {
            return JSONUtil.parseObj(configJson);
        } catch (Exception e) {
            log.error("解析抄送 nodeConfig 失败: taskId={}", task.getId(), e);
            return null;
        }
    }

    private String getCandidateParam(JSONObject nodeConfig) {
        Object obj = nodeConfig.get("candidateParam");
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONArray arr) {
            if (arr.isEmpty()) {
                return null;
            }
            // 抄送可能配置多个部门/用户，统一拼成逗号分隔供 resolver 解析
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(arr.getStr(i));
            }
            return sb.toString();
        }
        return obj.toString();
    }

    /**
     * 兼容部署后本地名 key（nodeConfig）与生成期命名空间 key（ns+name）
     */
    private String getExtensionText(ServiceTask task, String name) {
        Map<String, List<ExtensionElement>> extMap = task.getExtensionElements();
        if (extMap == null || extMap.isEmpty()) {
            return null;
        }
        List<ExtensionElement> elements = extMap.get(name);
        if (CollUtil.isEmpty(elements)) {
            elements = extMap.get("http://flowable.org/bpmn" + name);
        }
        if (CollUtil.isEmpty(elements)) {
            return null;
        }
        return elements.get(0).getElementText();
    }
}
