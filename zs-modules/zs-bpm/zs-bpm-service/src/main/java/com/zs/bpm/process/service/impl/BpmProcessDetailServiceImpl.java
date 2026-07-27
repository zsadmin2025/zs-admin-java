package com.zs.bpm.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zs.bpm.definition.domain.entity.BpmProcessDefinitionInfoEntity;
import com.zs.bpm.definition.service.IBpmProcessDefinitionInfoService;
import com.zs.bpm.form.domain.vo.FormDefinitionVO;
import com.zs.bpm.form.service.IBpmFormDefinitionService;
import com.zs.bpm.process.domain.vo.AssigneeUserVO;
import com.zs.bpm.process.service.IBpmProcessDetailService;
import com.zs.bpm.task.domain.params.TodoTaskParams;
import com.zs.bpm.task.domain.vo.*;
import com.zs.bpm.task.domain.vo.FlowNode;
import com.zs.common.core.constant.ProcessVariableConstants;
import com.zs.common.core.enums.bpmn.FlowNodeStatusEnum;
import com.zs.common.core.enums.bpmn.ProcessInstanceStateEnum;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BPM 流程详情查询 Service 实现
 * <p>
 * 查询流程详情全景视图，包括：流程实例基本信息、表单定义与数据、审批节点链、当前待办任务。
 * </p>
 *
 * @author zsadmin
 */
@Slf4j
@Service
public class BpmProcessDetailServiceImpl implements IBpmProcessDetailService {

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private HistoryService historyService;

    @Lazy
    @Resource
    private ISysUserService sysUserService;

    @Resource
    private IBpmProcessDefinitionInfoService processDefinitionInfoService;

    @Lazy
    @Resource
    private IBpmFormDefinitionService formDefinitionService;

    @Override
    public ProcessDetailVO getProcessDetail(TodoTaskParams params) {

        String processInstanceId = params.getProcessInstanceId();
        log.info("获取流程详情开始: processInstanceId={}", processInstanceId);

        // 1. 参数校验（仅需流程实例ID，无需任务ID）
        if (StrUtil.isBlank(processInstanceId)) {
            throw new ZsException("流程实例ID不能为空");
        }

        try {
            // 2. 获取流程实例（运行中 / 已结束均需兼容，统一从历史表读取）
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (historicProcessInstance == null) {
                log.error("流程实例不存在: processInstanceId={}", processInstanceId);
                throw new ZsException("流程实例不存在或已被清理");
            }
            String processDefinitionId = historicProcessInstance.getProcessDefinitionId();

            // 3. 获取流程定义信息
            ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
            if (processDefinition == null) {
                log.error("流程定义不存在: processDefinitionId={}", processDefinitionId);
                throw new ZsException("流程定义不存在");
            }

            // 4. 获取 BPMN 模型（用于解析节点链）
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

            // 5. 获取流程定义扩展信息（表单相关字段）
            BpmProcessDefinitionInfoEntity defInfoEntity = processDefinitionInfoService.getOne(
                    new LambdaQueryWrapper<BpmProcessDefinitionInfoEntity>()
                            .eq(BpmProcessDefinitionInfoEntity::getProcessDefinitionId, processDefinitionId));

            // 6. 查询历史活动实例（哪些节点已走过）
            List<HistoricActivityInstance> hisActivities = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .list();
            Map<String, HistoricActivityInstance> hisActMap = hisActivities.stream()
                    .collect(Collectors.toMap(
                            HistoricActivityInstance::getActivityId,
                            a -> a,
                            (a, b) -> a.getEndTime() != null ? a : b  // 多实例时优先取已完成的
                    ));

            // 7. 查询运行时任务（补全"进行中"节点的审批人）——按节点Key分组，会签/或签节点会有多个任务
            List<Task> runtimeTasks = taskService.createTaskQuery()
                    .processInstanceId(processInstanceId)
                    .list();
            Map<String, List<Task>> runtimeTaskMap = runtimeTasks.stream()
                    .collect(Collectors.groupingBy(Task::getTaskDefinitionKey));

            // 8. 查询全部历史已审批任务记录——按节点Key分组，会签节点会有多个已完成任务实例
            List<HistoricTaskInstance> hisTaskList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .finished()
                    .orderByHistoricTaskInstanceEndTime().asc()
                    .list();
            Map<String, List<HistoricTaskInstance>> hisTaskByDefKey = hisTaskList.stream()
                    .collect(Collectors.groupingBy(HistoricTaskInstance::getTaskDefinitionKey));

            // 9. 批量收集并查询相关用户信息（发起人 + 各节点审批人 / 委托人）
            //    注意：会签节点的审批人可能通过 IdentityLink 设置，assignee 可能为空
            Set<Long> userIds = new HashSet<>();
            collectUserId(userIds, historicProcessInstance.getStartUserId());
            for (Task rt : runtimeTasks) {
                collectUserId(userIds, rt.getAssignee());
                collectUserId(userIds, rt.getOwner());
                // 从 IdentityLink 获取候选人（会签节点 assignee 可能为空）
                List<IdentityLink> links = taskService.getIdentityLinksForTask(rt.getId());
                for (IdentityLink link : links) {
                    if (link.getUserId() != null) {
                        collectUserId(userIds, link.getUserId());
                    }
                }
            }
            for (HistoricTaskInstance ht : hisTaskList) {
                collectUserId(userIds, ht.getAssignee());
                collectUserId(userIds, ht.getOwner());
                // 从历史 IdentityLink 获取审批人（已完成任务的审批人记录）
                List<HistoricIdentityLink> hisLinks = historyService.getHistoricIdentityLinksForTask(ht.getId());
                for (HistoricIdentityLink link : hisLinks) {
                    if (link.getUserId() != null) {
                        collectUserId(userIds, link.getUserId());
                    }
                }
            }
            // 收集抄送节点（ServiceTask）的抄送人ID（act_hi_actinst.ASSIGNEE_ 为逗号分隔）
            for (HistoricActivityInstance act : hisActMap.values()) {
                if (StrUtil.isNotBlank(act.getAssignee())) {
                    for (String uid : act.getAssignee().split(",")) {
                        collectUserId(userIds, uid.trim());
                    }
                }
            }
            log.info("待查询用户ID列表: size={}, userIds={}", userIds.size(), userIds);
            Map<Long, SysUserVO> userMap = batchQueryUsers(userIds);
            log.info("用户查询结果: size={}", userMap != null ? userMap.size() : 0);

            // 10. 查询审批意见，按 taskId 聚合（同一任务多条意见用"；"拼接）
            List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
            Map<String, String> commentByTaskId = comments.stream()
                    .filter(c -> c.getTaskId() != null && StrUtil.isNotBlank(c.getFullMessage()))
                    .collect(Collectors.groupingBy(
                            Comment::getTaskId,
                            Collectors.mapping(Comment::getFullMessage, Collectors.joining("；"))
                    ));

            // 11. 加载流程变量作为表单数据（兼容运行中与已结束）
            Map<String, Object> formData = loadProcessVariables(processInstanceId);

            // 12. 组装返回
            ProcessDetailVO detailVO = new ProcessDetailVO();
            detailVO.setProcessDefinition(buildProcessDefinitionInfo(processDefinition, defInfoEntity));
            detailVO.setInstanceInfo(buildInstanceInfo(historicProcessInstance, processDefinition, userMap));
            detailVO.setFormInfo(buildFormInfo(processDefinition, defInfoEntity, formData));
            detailVO.setFlowNodes(buildFlowNodes(processInstanceId, processDefinition, bpmnModel,
                    hisActMap, hisTaskByDefKey, runtimeTaskMap, commentByTaskId, userMap, formData,
                    historicProcessInstance));

            // 当前待办任务：流程运行中时，取所有运行时任务构建 todoTask
            // 会签/或签时可能有多个待审批人，按节点 Key 取首个节点组的所有待办任务
            if (CollUtil.isNotEmpty(runtimeTasks)) {
                String firstNodeKey = runtimeTasks.get(0).getTaskDefinitionKey();
                List<Task> todoRuntimeTasks = runtimeTaskMap.get(firstNodeKey);
                if (CollUtil.isNotEmpty(todoRuntimeTasks)) {
                    FlowNode todoTask = buildTodoFlowNode(todoRuntimeTasks, processInstanceId,
                            processDefinition, bpmnModel, userMap, formData,
                            historicProcessInstance.getName(), historicProcessInstance.getBusinessKey());

                    // 权限检查：仅当前待办任务的合法审批人可见 todoTask
                    Long currentUserId = SecurityUtil.getUserId();
                    if (todoTask != null && CollUtil.isNotEmpty(todoTask.getApprovers())) {
                        boolean isAssignee = todoTask.getApprovers().stream()
                                .filter(a -> !FlowNodeStatusEnum.COMPLETED.getValue().equals(a.getStatus()))
                                .anyMatch(a -> a.getAssigneeUser() != null
                                        && currentUserId != null
                                        && String.valueOf(currentUserId).equals(a.getAssigneeUser().getStartUserId()));
                        if (!isAssignee) {
                            todoTask = null;
                        }
                    }
                    detailVO.setTodoTask(todoTask);
                }
            }

            log.info("获取流程详情成功: processInstanceId={}, flowNodesCount={}",
                    processInstanceId, detailVO.getFlowNodes() != null ? detailVO.getFlowNodes().size() : 0);
            return detailVO;
        } catch (ZsException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("获取流程详情发生异常: processInstanceId={}", processInstanceId, e);
            throw new ZsException("获取流程详情失败: " + e.getMessage());
        }
    }

    // ==================== 以下为私有辅助方法 ====================

    /**
     * 构建流程定义信息 VO
     * <p>
     * 优先从业务流程定义扩展表取，回退到 Flowable 引擎的 ProcessDefinition。
     */
    private ProcessDefinitionInfo buildProcessDefinitionInfo(ProcessDefinition processDefinition,
                                                             BpmProcessDefinitionInfoEntity defInfoEntity) {
        ProcessDefinitionInfo info = new ProcessDefinitionInfo();
        if (defInfoEntity != null) {
            info.setId(defInfoEntity.getId());
            info.setProcessDefinitionId(defInfoEntity.getProcessDefinitionId());
            info.setDeploymentId(defInfoEntity.getDeploymentId());
            info.setModelId(defInfoEntity.getModelId());
            info.setProcessKey(defInfoEntity.getProcessKey());
            info.setProcessName(defInfoEntity.getProcessName());
            info.setCategoryId(defInfoEntity.getCategoryId());
            info.setIcon(defInfoEntity.getIcon());
            info.setDescription(defInfoEntity.getDescription());
            info.setVersion(defInfoEntity.getVersion());
            info.setFormId(defInfoEntity.getFormId());
            info.setFormType(defInfoEntity.getFormType());
            info.setFormRule(defInfoEntity.getFormRule());
            info.setFormOption(defInfoEntity.getFormOption());
            info.setModelJson(defInfoEntity.getModelJson());
            info.setBpmnXml(defInfoEntity.getBpmnXml());
            info.setStatus(defInfoEntity.getStatus());
            info.setPublishTime(defInfoEntity.getPublishTime());
            info.setCategoryName(defInfoEntity.getCategoryName());
        }
        // 扩展表为空时，从 Flowable 引擎定义兜底
        if (StrUtil.isBlank(info.getProcessKey()) && processDefinition != null) {
            info.setProcessKey(processDefinition.getKey());
        }
        if (StrUtil.isBlank(info.getProcessName()) && processDefinition != null) {
            info.setProcessName(processDefinition.getName());
        }
        if (info.getVersion() == null && processDefinition != null) {
            info.setVersion(processDefinition.getVersion());
        }
        if (StrUtil.isBlank(info.getDeploymentId()) && processDefinition != null) {
            info.setDeploymentId(processDefinition.getDeploymentId());
        }
        return info;
    }

    /**
     * 收集用户ID到集合中（忽略非数字字符串）
     */
    private void collectUserId(Set<Long> userIds, String userIdStr) {
        if (StrUtil.isBlank(userIdStr)) {
            return;
        }
        try {
            userIds.add(Long.parseLong(userIdStr));
        } catch (NumberFormatException ignored) {
            // 非数字ID（如表达式占位符）忽略
        }
    }

    /**
     * 批量查询用户信息
     */
    private Map<Long, SysUserVO> batchQueryUsers(Set<Long> userIdSet) {
        if (userIdSet.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            List<SysUserVO> users = sysUserService.getUserList(userIdSet.toArray(Long[]::new));
            if (users == null) {
                return Collections.emptyMap();
            }
            return users.stream()
                    .filter(u -> u.getSysUserId() != null)
                    .collect(Collectors.toMap(SysUserVO::getSysUserId, u -> u, (a, b) -> a));
        } catch (Exception e) {
            log.error("批量查询用户信息失败", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 加载流程变量（兼容运行中和已结束的流程实例）
     * <p>
     * 优先读取 {@code formDataJson}（JSON 序列化存储，天然隔离 Flowable 内部变量），
     * 兼容旧流程（无 formDataJson 时走原有过滤逻辑）。
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> loadProcessVariables(String processInstanceId) {
        try {
            // 优先从 formDataJson 提取（新流程），彻底避免混入 Flowable 内部变量
            HistoricVariableInstance formDataVar = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .variableName("formDataJson")
                    .singleResult();
            if (formDataVar != null && formDataVar.getValue() != null) {
                String json = formDataVar.getValue().toString();
                Map<String, Object> formData = JSONUtil.toBean(json, Map.class);
                if (CollUtil.isNotEmpty(formData)) {
                    return formData;
                }
            }

            // 兼容旧流程：遍历所有变量并过滤框架内置变量
            List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .list();
            if (CollUtil.isEmpty(variables)) {
                return Collections.emptyMap();
            }
            Map<String, Object> result = new HashMap<>();
            for (HistoricVariableInstance v : variables) {
                if (StrUtil.isNotBlank(v.getVariableName())) {
                    result.put(v.getVariableName(), v.getValue());
                }
            }
            result.remove(ProcessVariableConstants.FLOWABLE_SKIP_EXPRESSION_ENABLED);
            result.remove("startUserId");
            result.remove("_SKIP_INITIATOR");
            return result;
        } catch (Exception e) {
            log.error("加载流程变量失败: processInstanceId={}", processInstanceId, e);
            return Collections.emptyMap();
        }
    }

    /**
     * 构建流程实例基本信息
     */
    private ProcessInstanceInfo buildInstanceInfo(HistoricProcessInstance hpi,
                                                  ProcessDefinition processDefinition,
                                                  Map<Long, SysUserVO> userMap) {
        ProcessInstanceInfo info = new ProcessInstanceInfo();
        info.setProcessInstanceId(hpi.getId());
        info.setProcessDefinitionId(processDefinition.getId());
        info.setProcessDefinitionName(processDefinition.getName());
        info.setProcessDefinitionKey(processDefinition.getKey());
        info.setProcessInstanceName(hpi.getName());
        info.setBusinessKey(hpi.getBusinessKey());
        info.setStartTime(hpi.getStartTime());
        info.setEndTime(hpi.getEndTime());
        info.setDurationInMillis(hpi.getDurationInMillis());

        // 流程状态：运行中 / 已结束 / 已作废
        if (hpi.getEndTime() == null) {
            info.setProcessState(ProcessInstanceStateEnum.RUNNING.getValue());
        } else if (StrUtil.isNotBlank(hpi.getDeleteReason())) {
            info.setProcessState(ProcessInstanceStateEnum.CANCELLED.getValue());
        } else {
            info.setProcessState(ProcessInstanceStateEnum.COMPLETED.getValue());
        }

        // 发起人信息
        if (StrUtil.isNotBlank(hpi.getStartUserId())) {
            try {
                SysUserVO user = userMap.get(Long.parseLong(hpi.getStartUserId()));
                if (user != null) {
                    info.setStartUser(AssigneeUserVO.from(user));
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return info;
    }

    /**
     * 构建表单信息（表单定义 + 表单数据）
     */
    private FormInfoVO buildFormInfo(ProcessDefinition processDefinition,
                                     BpmProcessDefinitionInfoEntity defInfoEntity,
                                     Map<String, Object> formData) {
        FormInfoVO formInfo = new FormInfoVO();
        formInfo.setProcessKey(processDefinition.getKey());
        formInfo.setProcessName(processDefinition.getName());
        formInfo.setFormData(formData);

        if (defInfoEntity != null) {
            formInfo.setFormId(defInfoEntity.getFormId());
            formInfo.setFormType(defInfoEntity.getFormType());
            formInfo.setFormRule(defInfoEntity.getFormRule());
            formInfo.setFormOption(defInfoEntity.getFormOption());
            formInfo.setVersion(defInfoEntity.getVersion());

            // 查询表单名称
            if (defInfoEntity.getFormId() != null) {
                try {
                    FormDefinitionVO form = formDefinitionService.getById(defInfoEntity.getFormId());
                    if (form != null) {
                        formInfo.setFormName(form.getFormName());
                    }
                } catch (Exception e) {
                    log.warn("查询表单定义失败: formId={}", defInfoEntity.getFormId(), e);
                }
            }
        }
        return formInfo;
    }

    /**
     * 构建流程节点列表（按 开始 → 审批节点 →结束 顺序排列）
     * <p>
     * 仅包含实际流转经过的节点（有历史活动实例）或当前运行时任务，
     * 排他网关未命中的分支节点不会输出，避免展示未走的分支。
     */
    private List<FlowNode> buildFlowNodes(String processInstanceId,
                                          ProcessDefinition processDefinition,
                                          BpmnModel bpmnModel,
                                          Map<String, HistoricActivityInstance> hisActMap,
                                          Map<String, List<HistoricTaskInstance>> hisTaskByDefKey,
                                          Map<String, List<Task>> runtimeTaskMap,
                                          Map<String, String> commentByTaskId,
                                          Map<Long, SysUserVO> userMap,
                                          Map<String, Object> formData,
                                          HistoricProcessInstance hpi) {
        if (bpmnModel == null || bpmnModel.getMainProcess() == null) {
            log.warn("BPMN模型为空，无法构建节点列表: processDefinitionId={}", processDefinition.getId());
            return Collections.emptyList();
        }
        List<FlowNode> nodes = new ArrayList<>();

        // 开始节点：流程已启动则开始节点必然已走过
        String processInstanceName = hpi.getName();
        String businessKey = hpi.getBusinessKey();
        List<StartEvent> startEvents = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class);
        for (StartEvent startEvent : startEvents) {
            if (hisActMap.containsKey(startEvent.getId())) {
                nodes.add(buildStartFlowNode(startEvent, processInstanceId, processDefinition, hisActMap,
                        processInstanceName, businessKey));
            }
        }

        // 用户任务节点：仅包含已走过（有历史活动）或当前进行中（有运行时任务）的节点
        List<UserTask> userTasks = bpmnModel.getMainProcess().findFlowElementsOfType(UserTask.class);
        for (UserTask userTask : userTasks) {
            String nodeKey = userTask.getId();
            if (hisActMap.containsKey(nodeKey) || runtimeTaskMap.containsKey(nodeKey)
                    || hisTaskByDefKey.containsKey(nodeKey)) {
                nodes.add(buildUserTaskFlowNode(userTask, processInstanceId, processDefinition,
                        hisActMap, hisTaskByDefKey, runtimeTaskMap, commentByTaskId, userMap, formData,
                        processInstanceName, businessKey));
            }
        }

        // 抄送节点（ServiceTask）：仅包含已走过的抄送节点
        List<ServiceTask> serviceTasks = bpmnModel.getMainProcess().findFlowElementsOfType(ServiceTask.class);
        for (ServiceTask serviceTask : serviceTasks) {
            String nodeKey = serviceTask.getId();
            if (hisActMap.containsKey(nodeKey)) {
                nodes.add(buildServiceTaskFlowNode(serviceTask, processInstanceId, processDefinition,
                        hisActMap, userMap, processInstanceName, businessKey));
            }
        }

        // 结束节点：仅包含已到达的（流程已结束）
        List<EndEvent> endEvents = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        for (EndEvent endEvent : endEvents) {
            if (hisActMap.containsKey(endEvent.getId())) {
                nodes.add(buildEndFlowNode(endEvent, processInstanceId, processDefinition, hisActMap,
                        processInstanceName, businessKey));
            }
        }

        return nodes;
    }

    /**
     * 构建开始节点
     */
    private FlowNode buildStartFlowNode(StartEvent startEvent, String processInstanceId,
                                        ProcessDefinition processDefinition,
                                        Map<String, HistoricActivityInstance> hisActMap,
                                        String processInstanceName, String businessKey) {
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(processInstanceId);
        node.setProcessDefinitionId(processDefinition.getId());
        node.setProcessDefinitionKey(processDefinition.getKey());
        node.setProcessDefinitionName(processDefinition.getName());
        node.setProcessInstanceName(processInstanceName);
        node.setBusinessKey(businessKey);
        node.setNodeKey(startEvent.getId());
        node.setNodeName(StrUtil.isNotBlank(startEvent.getName()) ? startEvent.getName() : "开始");
        node.setStatus(FlowNodeStatusEnum.COMPLETED.getValue());

        HistoricActivityInstance act = hisActMap.get(startEvent.getId());
        if (act != null) {
            node.setTaskId(act.getId());
            node.setStartTime(act.getStartTime());
            node.setEndTime(act.getEndTime());
            if (act.getDurationInMillis() != null) {
                node.setDurationInMillis(act.getDurationInMillis());
            }
        }
        return node;
    }

    /**
     * 构建用户任务节点（审批节点），区分已完成 / 进行中 / 未到达。
     * <p>
     * 会签/或签节点会产生多个任务实例，每个实例对应一个 {@link NodeApprover}，
     * 全部收集到 {@code approvers} 列表中；节点级字段（status/时间/意见）做汇总。
     * </p>
     */
    private FlowNode buildUserTaskFlowNode(UserTask userTask, String processInstanceId,
                                           ProcessDefinition processDefinition,
                                           Map<String, HistoricActivityInstance> hisActMap,
                                           Map<String, List<HistoricTaskInstance>> hisTaskByDefKey,
                                           Map<String, List<Task>> runtimeTaskMap,
                                           Map<String, String> commentByTaskId,
                                           Map<Long, SysUserVO> userMap,
                                           Map<String, Object> formData,
                                           String processInstanceName, String businessKey) {
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(processInstanceId);
        node.setProcessDefinitionId(processDefinition.getId());
        node.setProcessDefinitionKey(processDefinition.getKey());
        node.setProcessDefinitionName(processDefinition.getName());
        node.setProcessInstanceName(processInstanceName);
        node.setBusinessKey(businessKey);
        node.setNodeKey(userTask.getId());
        node.setNodeName(userTask.getName());
        node.setDescription(userTask.getDocumentation());

        String nodeKey = userTask.getId();
        List<HistoricTaskInstance> hisTasks = hisTaskByDefKey.getOrDefault(nodeKey, Collections.emptyList());
        List<Task> runtimeTaskList = runtimeTaskMap.getOrDefault(nodeKey, Collections.emptyList());

        List<NodeApprover> approvers = new ArrayList<>();

        // 已完成的任务实例（会签节点可能有多个）
        for (HistoricTaskInstance his : hisTasks) {
            NodeApprover approver = new NodeApprover();
            approver.setTaskId(his.getId());
            approver.setStartTime(his.getStartTime());
            approver.setEndTime(his.getEndTime());
            approver.setDurationInMillis(his.getDurationInMillis());
            approver.setStatus(FlowNodeStatusEnum.COMPLETED.getValue());

            approver.setComment(commentByTaskId.get(his.getId()));

            // 优先从 assignee 获取，否则从历史 IdentityLink 获取，最后从 HistoricActivityInstance 兜底
            if (StrUtil.isNotBlank(his.getAssignee())) {
                approver.setAssigneeUser(buildAssigneeUser(his.getAssignee(), userMap));
            } else {
                // 从历史 IdentityLink 获取审批人（会签节点 assignee 可能为空）
                List<HistoricIdentityLink> hisLinks = historyService.getHistoricIdentityLinksForTask(his.getId());
                boolean foundFromLink = false;
                for (HistoricIdentityLink link : hisLinks) {
                    if (link.getUserId() != null) {
                        approver.setAssigneeUser(buildAssigneeUser(link.getUserId(), userMap));
                        foundFromLink = true;
                        break;
                    }
                }
                // 兜底：从 HistoricActivityInstance 获取审批人（complete 参数错误时任务 assignee 可能丢失）
                if (!foundFromLink) {
                    HistoricActivityInstance act = hisActMap.get(his.getTaskDefinitionKey());
                    if (act != null && StrUtil.isNotBlank(act.getAssignee())) {
                        approver.setAssigneeUser(buildAssigneeUser(act.getAssignee(), userMap));
                    }
                }
            }
            if (StrUtil.isNotBlank(his.getOwner())) {
                approver.setOriginalAssigneeUser(buildAssigneeUser(his.getOwner(), userMap));
            }
            approvers.add(approver);
        }

        // 进行中的任务实例（会签/或签节点可能有多个待审批人）
        for (Task rt : runtimeTaskList) {
            NodeApprover approver = new NodeApprover();
            approver.setTaskId(rt.getId());
            approver.setStartTime(rt.getCreateTime());
            approver.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getValue());

            // 优先从 assignee 获取，否则从 IdentityLink 获取
            if (StrUtil.isNotBlank(rt.getAssignee())) {
                approver.setAssigneeUser(buildAssigneeUser(rt.getAssignee(), userMap));
            } else {
                // 从 IdentityLink 获取审批人（会签节点 assignee 可能为空）
                List<IdentityLink> links = taskService.getIdentityLinksForTask(rt.getId());
                for (IdentityLink link : links) {
                    if (link.getUserId() != null) {
                        approver.setAssigneeUser(buildAssigneeUser(link.getUserId(), userMap));
                        break;
                    }
                }
            }
            if (StrUtil.isNotBlank(rt.getOwner())) {
                approver.setOriginalAssigneeUser(buildAssigneeUser(rt.getOwner(), userMap));
            }
            approvers.add(approver);
        }

        node.setApprovers(approvers);

        // 节点级任务ID：取首个审批人的任务ID（会签/或签节点取第一个）
        if (!approvers.isEmpty()) {
            node.setTaskId(approvers.get(0).getTaskId());
        }

        // 节点级状态与时间汇总（审批人详情已在 approvers 列表中，节点级不再重复）
        if (approvers.isEmpty()) {
            node.setStatus(FlowNodeStatusEnum.NOT_STARTED.getValue());
        } else {
            boolean hasInProgress = approvers.stream().anyMatch(a -> FlowNodeStatusEnum.IN_PROGRESS.getValue().equals(a.getStatus()));
            node.setStatus(hasInProgress ? FlowNodeStatusEnum.IN_PROGRESS.getValue() : FlowNodeStatusEnum.COMPLETED.getValue());

            // 时间范围：取所有审批人中最早开始 / 最晚结束
            node.setStartTime(approvers.stream()
                    .map(NodeApprover::getStartTime)
                    .filter(d -> d != null)
                    .min(Date::compareTo)
                    .orElse(null));
            node.setEndTime(approvers.stream()
                    .map(NodeApprover::getEndTime)
                    .filter(d -> d != null)
                    .max(Date::compareTo)
                    .orElse(null));

            // 耗时：直接取 Flowable 活动实例的 durationInMillis
            HistoricActivityInstance act = hisActMap.get(nodeKey);
            if (act != null && act.getDurationInMillis() != null) {
                node.setDurationInMillis(act.getDurationInMillis());
            }
        }

        // 节点权限配置（按钮权限 + 字段权限）
        node.setPermissionConfig(buildPermissionConfig(userTask));
        return node;
    }

    /**
     * 构建结束节点
     */
    private FlowNode buildEndFlowNode(EndEvent endEvent, String processInstanceId,
                                      ProcessDefinition processDefinition,
                                      Map<String, HistoricActivityInstance> hisActMap,
                                      String processInstanceName, String businessKey) {
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(processInstanceId);
        node.setProcessDefinitionId(processDefinition.getId());
        node.setProcessDefinitionKey(processDefinition.getKey());
        node.setProcessDefinitionName(processDefinition.getName());
        node.setProcessInstanceName(processInstanceName);
        node.setBusinessKey(businessKey);
        node.setNodeKey(endEvent.getId());
        node.setNodeName(StrUtil.isNotBlank(endEvent.getName()) ? endEvent.getName() : "结束");

        HistoricActivityInstance act = hisActMap.get(endEvent.getId());
        if (act != null && act.getEndTime() != null) {
            node.setStartTime(act.getStartTime());
            node.setEndTime(act.getEndTime());
            if (act.getDurationInMillis() != null) {
                node.setDurationInMillis(act.getDurationInMillis());
            }
            node.setStatus(FlowNodeStatusEnum.COMPLETED.getValue());
        } else {
            node.setStatus(FlowNodeStatusEnum.NOT_STARTED.getValue());
        }
        return node;
    }

    /**
     * 构建抄送节点（ServiceTask），解析 ASSIGNEE_ 中的抄送人列表。
     * <p>
     * 抄送节点为 ServiceTask 类型，不生成任务实例（act_hi_taskinst），
     * 抄送人信息由 {@code BpmCopyTaskDelegate} 回写到 act_hi_actinst.ASSIGNEE_。
     * </p>
     */
    private FlowNode buildServiceTaskFlowNode(ServiceTask serviceTask, String processInstanceId,
                                              ProcessDefinition processDefinition,
                                              Map<String, HistoricActivityInstance> hisActMap,
                                              Map<Long, SysUserVO> userMap,
                                              String processInstanceName, String businessKey) {
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(processInstanceId);
        node.setProcessDefinitionId(processDefinition.getId());
        node.setProcessDefinitionKey(processDefinition.getKey());
        node.setProcessDefinitionName(processDefinition.getName());
        node.setProcessInstanceName(processInstanceName);
        node.setBusinessKey(businessKey);
        node.setNodeKey(serviceTask.getId());
        node.setNodeName(StrUtil.isNotBlank(serviceTask.getName()) ? serviceTask.getName() : "抄送");
        node.setStatus(FlowNodeStatusEnum.COMPLETED.getValue());

        HistoricActivityInstance act = hisActMap.get(serviceTask.getId());
        if (act != null) {
            node.setTaskId(act.getId());
            node.setStartTime(act.getStartTime());
            node.setEndTime(act.getEndTime());
            if (act.getDurationInMillis() != null) {
                node.setDurationInMillis(act.getDurationInMillis());
            }

            // 解析抄送人：ASSIGNEE_ 为逗号分隔的用户ID列表
            if (StrUtil.isNotBlank(act.getAssignee())) {
                List<NodeApprover> approvers = new ArrayList<>();
                for (String userIdStr : act.getAssignee().split(",")) {
                    String trimmed = userIdStr.trim();
                    if (StrUtil.isBlank(trimmed)) {
                        continue;
                    }
                    NodeApprover approver = new NodeApprover();
                    approver.setTaskId(act.getId());
                    approver.setAssigneeUser(buildAssigneeUser(trimmed, userMap));
                    approver.setStartTime(act.getStartTime());
                    approver.setEndTime(act.getEndTime());
                    approver.setStatus(FlowNodeStatusEnum.COMPLETED.getValue());
                    approvers.add(approver);
                }
                node.setApprovers(approvers);
            }
        }
        return node;
    }

    /**
     * 构建当前待办任务节点（FlowNode 形式，对应 ProcessDetailVO.todoTask）。
     * <p>
     * 会签/或签节点当前可能有多个待审批任务实例，全部收集到 {@code approvers} 列表。
     * </p>
     *
     * @param todoRuntimeTasks 当前待办节点的所有运行时任务实例（至少1个）
     */
    private FlowNode buildTodoFlowNode(List<Task> todoRuntimeTasks, String processInstanceId,
                                       ProcessDefinition processDefinition,
                                       BpmnModel bpmnModel,
                                       Map<Long, SysUserVO> userMap,
                                       Map<String, Object> formData,
                                       String processInstanceName, String businessKey) {
        Task firstTask = todoRuntimeTasks.get(0);
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(processInstanceId);
        node.setProcessDefinitionId(processDefinition.getId());
        node.setProcessDefinitionKey(processDefinition.getKey());
        node.setProcessDefinitionName(processDefinition.getName());
        node.setProcessInstanceName(processInstanceName);
        node.setBusinessKey(businessKey);
        node.setNodeKey(firstTask.getTaskDefinitionKey());
        node.setNodeName(firstTask.getName());
        node.setDescription(firstTask.getDescription());
        node.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getValue());

        // 构建审批人列表（多实例时可能有多个待审批人）
        List<NodeApprover> approvers = new ArrayList<>();
        for (Task rt : todoRuntimeTasks) {
            NodeApprover approver = new NodeApprover();
            approver.setTaskId(rt.getId());
            approver.setStartTime(rt.getCreateTime());
            approver.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getValue());

            // 优先从 assignee 获取，否则从 IdentityLink 获取（会签节点 assignee 可能为空）
            if (StrUtil.isNotBlank(rt.getAssignee())) {
                approver.setAssigneeUser(buildAssigneeUser(rt.getAssignee(), userMap));
            } else {
                List<IdentityLink> links = taskService.getIdentityLinksForTask(rt.getId());
                for (IdentityLink link : links) {
                    if (link.getUserId() != null) {
                        approver.setAssigneeUser(buildAssigneeUser(link.getUserId(), userMap));
                        break;
                    }
                }
            }
            if (StrUtil.isNotBlank(rt.getOwner())) {
                approver.setOriginalAssigneeUser(buildAssigneeUser(rt.getOwner(), userMap));
            }
            approvers.add(approver);
        }
        node.setApprovers(approvers);

        // 节点级任务ID：取首个审批人的任务ID（会签/或签节点取第一个）
        if (!approvers.isEmpty()) {
            node.setTaskId(approvers.get(0).getTaskId());
        }

        // 节点级时间：取最早开始时间（审批人详情已在 approvers 列表中，节点级不再重复）
        node.setStartTime(approvers.stream()
                .map(NodeApprover::getStartTime)
                .filter(d -> d != null)
                .min(Date::compareTo)
                .orElse(firstTask.getCreateTime()));

        // 节点权限配置：根据 taskDefinitionKey 反查 BPMN 模型中的 UserTask 定义
        if (bpmnModel != null && bpmnModel.getMainProcess() != null
                && StrUtil.isNotBlank(firstTask.getTaskDefinitionKey())) {
            FlowElement flowElement = bpmnModel.getMainProcess().getFlowElement(firstTask.getTaskDefinitionKey());
            if (flowElement instanceof UserTask userTask) {
                node.setPermissionConfig(buildPermissionConfig(userTask));
            }
        }
        return node;
    }

    /**
     * 根据用户ID构建审批人简要信息
     */
    private AssigneeUserVO buildAssigneeUser(String userIdStr, Map<Long, SysUserVO> userMap) {
        if (StrUtil.isBlank(userIdStr)) {
            return null;
        }
        try {
            Long userId = Long.parseLong(userIdStr);
            SysUserVO user = userMap.get(userId);
            return AssigneeUserVO.from(user);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 构建节点权限配置（按钮权限 + 字段权限）
     * <p>
     * 解析 BPMN 扩展元素，优先读取 JSON CDATA 格式（buttonsSettingJson / fieldsPermissionJson），
     * 回退读取单个扩展元素的属性（buttonsSetting / fieldsPermission）。
     *
     * @param userTask 用户任务节点
     * @return 节点权限配置，无配置时返回空对象（buttonPermissions / fieldPermissions 均为空列表）
     */
    private NodePermissionConfig buildPermissionConfig(UserTask userTask) {
        NodePermissionConfig config = new NodePermissionConfig();
        if (userTask == null || userTask.getExtensionElements() == null
                || userTask.getExtensionElements().isEmpty()) {
            return config;
        }
        try {
            config.setButtonPermissions(parseButtonPermissions(userTask));
            config.setFieldPermissions(parseFieldPermissions(userTask));
        } catch (Exception e) {
            log.warn("解析节点权限配置失败: nodeKey={}, nodeName={}", userTask.getId(), userTask.getName(), e);
        }
        return config;
    }

    /**
     * 解析按钮权限配置
     */
    private List<ButtonPermission> parseButtonPermissions(UserTask userTask) {
        List<ButtonPermission> result = new ArrayList<>();

        // 方式1：buttonsSettingJson（CDATA JSON 数组，优先）
        String json = readExtensionElementText(userTask, "buttonsSettingJson");
        if (StrUtil.isNotBlank(json)) {
            try {
                JSONArray arr = JSONUtil.parseArray(json);
                for (Object obj : arr) {
                    JSONObject jo = (JSONObject) obj;
                    ButtonPermission bp = new ButtonPermission();
                    bp.setId(jo.getStr("id"));
                    bp.setDisplayName(jo.getStr("displayName"));
                    bp.setEnable(Boolean.TRUE.equals(jo.getBool("enable")));
                    result.add(bp);
                }
                if (!result.isEmpty()) {
                    return result;
                }
            } catch (Exception e) {
                log.warn("解析 buttonsSettingJson 失败: nodeKey={}", userTask.getId(), e);
            }
        }

        // 方式2：buttonsSetting 单个扩展元素（通过属性读取）
        List<ExtensionElement> elements = userTask.getExtensionElements().get("buttonsSetting");
        if (elements != null) {
            for (ExtensionElement el : elements) {
                ButtonPermission bp = new ButtonPermission();
                bp.setId(getExtAttrValue(el, "id"));
                bp.setDisplayName(getExtAttrValue(el, "displayName"));
                bp.setEnable(Boolean.parseBoolean(getExtAttrValue(el, "enable")));
                result.add(bp);
            }
        }
        return result;
    }

    /**
     * 解析字段权限配置
     */
    private List<FieldPermission> parseFieldPermissions(UserTask userTask) {
        List<FieldPermission> result = new ArrayList<>();

        // 方式1：fieldsPermissionJson（CDATA JSON 数组，优先）
        String json = readExtensionElementText(userTask, "fieldsPermissionJson");
        if (StrUtil.isNotBlank(json)) {
            try {
                JSONArray arr = JSONUtil.parseArray(json);
                for (Object obj : arr) {
                    JSONObject jo = (JSONObject) obj;
                    FieldPermission fp = new FieldPermission();
                    fp.setField(jo.getStr("field"));
                    fp.setTitle(jo.getStr("title"));
                    fp.setPermission(jo.getStr("permission"));
                    result.add(fp);
                }
                if (!result.isEmpty()) {
                    return result;
                }
            } catch (Exception e) {
                log.warn("解析 fieldsPermissionJson 失败: nodeKey={}", userTask.getId(), e);
            }
        }

        // 方式2：fieldsPermission 单个扩展元素（通过属性读取）
        List<ExtensionElement> elements = userTask.getExtensionElements().get("fieldsPermission");
        if (elements != null) {
            for (ExtensionElement el : elements) {
                FieldPermission fp = new FieldPermission();
                fp.setField(getExtAttrValue(el, "field"));
                fp.setTitle(getExtAttrValue(el, "title"));
                fp.setPermission(getExtAttrValue(el, "permission"));
                result.add(fp);
            }
        }
        return result;
    }

    /**
     * 读取 BPMN 扩展元素的文本内容（CDATA）
     *
     * @param element     BPMN 元素
     * @param elementName 扩展元素名称
     * @return 文本内容，不存在时返回 null
     */
    private String readExtensionElementText(FlowElement element, String elementName) {
        if (element == null || element.getExtensionElements() == null || StrUtil.isBlank(elementName)) {
            return null;
        }
        List<ExtensionElement> elements = element.getExtensionElements().get(elementName);
        if (elements != null && !elements.isEmpty()) {
            return elements.get(0).getElementText();
        }
        return null;
    }

    /**
     * 获取扩展元素的属性值（取首个同名属性）
     *
     * @param element       扩展元素
     * @param attributeName 属性名称
     * @return 属性值，不存在时返回 null
     */
    private String getExtAttrValue(ExtensionElement element, String attributeName) {
        if (element == null || element.getAttributes() == null || StrUtil.isBlank(attributeName)) {
            return null;
        }
        List<ExtensionAttribute> attrs = element.getAttributes().get(attributeName);
        if (attrs != null && !attrs.isEmpty()) {
            return attrs.get(0).getValue();
        }
        return null;
    }
}
