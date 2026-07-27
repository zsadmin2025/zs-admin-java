package com.zs.bpm.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.zs.bpm.process.domain.vo.*;
import com.zs.bpm.process.service.IBpmProcessInstanceService;
import com.zs.bpm.task.domain.params.TaskPageQueryParams;
import com.zs.common.core.enums.bpmn.FlowableActivityTypeEnum;
import com.zs.common.core.enums.bpmn.ProcessInstanceStateEnum;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.task.api.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程实例管理 Service 实现
 *
 * @author zsadmin
 */
@Service
public class BpmProcessInstanceServiceImpl implements IBpmProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Lazy
    @Resource
    private ISysUserService sysUserService;

    @Override
    
    public PageResult<ProcessInstanceVO> getAllProcessInstance(TaskPageQueryParams params) {
        // 1. 查询历史流程实例（流程变量 + 租户过滤）
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .processInstanceTenantId(TenantContext.getTenantId())  
                .orderByProcessInstanceStartTime()
                .desc();

        long total = query.count();
        int current = (int) params.getCurrent();
        int pageSize = (int) params.getPageSize();
        int offset = (current - 1) * pageSize;
        List<HistoricProcessInstance> instances = query.listPage(offset, pageSize);

        return new PageResult<>(toVOList(instances), total);
    }


    @Override
    public PageResult<ProcessInstanceVO> myProcesses(TaskPageQueryParams params) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .startedBy(String.valueOf(SecurityUtil.getUserId()))
                .processInstanceTenantId(TenantContext.getTenantId())  
                .orderByProcessInstanceStartTime()
                .desc();



        // 可选搜索条件
        if (StrUtil.isNotBlank(params.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + params.getProcessDefinitionName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessDefinitionKey())) {
            query.processDefinitionKeyLike("%" + params.getProcessDefinitionKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getTaskName())) {
            query.processInstanceNameLike("%" + params.getTaskName() + "%");
        }
        if (StrUtil.isNotBlank(params.getBusinessKey())) {
            query.processInstanceBusinessKeyLike("%" + params.getBusinessKey() + "%");
        }

        long total = query.count();
        int current = (int) params.getCurrent();
        int pageSize = (int) params.getPageSize();
        int offset = (current - 1) * pageSize;
        List<HistoricProcessInstance> instances = query.listPage(offset, pageSize);

        return new PageResult<>(toVOList(instances), total);
    }

    /**
     * 将 HistoricProcessInstance 列表转换为 ProcessInstanceVO 列表
     * <p>
     * 提取公共转换逻辑，供 getAllProcessInstance / myProcesses 复用
     */
    private List<ProcessInstanceVO> toVOList(List<HistoricProcessInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集所有发起人 userId 批量查询
        Set<Long> startUserIds = new HashSet<>();
        for (HistoricProcessInstance instance : instances) {
            if (instance.getStartUserId() != null) {
                try {
                    startUserIds.add(Long.parseLong(instance.getStartUserId()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        Map<Long, SysUserVO> userMap = batchQueryUsers(startUserIds);

        List<ProcessInstanceVO> voList = new ArrayList<>();
        for (HistoricProcessInstance instance : instances) {
            ProcessInstanceVO vo = new ProcessInstanceVO();
            vo.setProcessInstanceId(instance.getId());
            vo.setProcessDefinitionId(instance.getProcessDefinitionId());
            vo.setProcessDefinitionKey(instance.getProcessDefinitionKey());
            vo.setProcessDefinitionName(instance.getProcessDefinitionName());
            vo.setProcessInstanceName(instance.getName());
            vo.setBusinessKey(instance.getBusinessKey());
            vo.setStartUserId(instance.getStartUserId());
            vo.setStartTime(instance.getStartTime());
            vo.setEndTime(instance.getEndTime());
            vo.setDurationInMillis(instance.getDurationInMillis());

            // 流程状态：未结束=RUNNING，已结束且无删除原因=FINISHED，已结束且有删除原因=CANCELED
            if (instance.getEndTime() == null) {
                vo.setStatus(ProcessInstanceStateEnum.RUNNING);
            } else if (instance.getDeleteReason() == null) {
                vo.setStatus(ProcessInstanceStateEnum.COMPLETED);
            } else {
                vo.setStatus(ProcessInstanceStateEnum.CANCELLED);
            }

            // 发起人信息
            if (instance.getStartUserId() != null) {
                try {
                    Long suid = Long.parseLong(instance.getStartUserId());
                    SysUserVO startUser = userMap.get(suid);
                    if (startUser != null) {
                        vo.setStartUserName(startUser.getRealName() != null ? startUser.getRealName() : startUser.getUsername());
                        vo.setStartDeptId(startUser.getSysDeptId() != null ? String.valueOf(startUser.getSysDeptId()) : null);
                        vo.setStartDeptName(startUser.getDeptName());
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            vo.setCurrentTasks(getCurrentTaskByProcInstId(instance.getId()));
            voList.add(vo);
        }

        // 补充审批人姓名：收集所有当前任务审批人 ID 并批量查询
        Set<Long> assigneeIds = new HashSet<>();
        for (ProcessInstanceVO vo : voList) {
            if (CollUtil.isNotEmpty(vo.getCurrentTasks())) {
                for (TaskVO task : vo.getCurrentTasks()) {
                    if (StrUtil.isNotBlank(task.getAssignee())) {
                        try {
                            assigneeIds.add(Long.parseLong(task.getAssignee()));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }
        if (!assigneeIds.isEmpty()) {
            Map<Long, SysUserVO> assigneeUserMap = batchQueryUsers(assigneeIds);
            for (ProcessInstanceVO vo : voList) {
                if (CollUtil.isNotEmpty(vo.getCurrentTasks())) {
                    vo.setAssigneeName(vo.getCurrentTasks().stream()
                            .map(TaskVO::getAssignee)
                            .filter(StrUtil::isNotBlank)
                            .map(id -> {
                                try {
                                    SysUserVO u = assigneeUserMap.get(Long.parseLong(id));
                                    return u != null ? u.getRealName() : null;
                                } catch (NumberFormatException e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(", ")));
                }
            }
        }

        return voList;
    }

    /**
     * 查询该流程实例下所有待审批任务（多会签/并行会返回多条）
     * <p>
     * 注意：不直接返回 Flowable Task 实体，避免 Jackson 序列化时触发懒加载异常
     */
    public List<TaskVO> getCurrentTaskByProcInstId(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime().asc()
                .list();


        List<TaskVO> result = tasks.stream().map(TaskVO::convert).collect(Collectors.toList());
        return result;
    }





    @Override
    public PageResult<ProcessInstance> getRunningProcessInstance(TaskPageQueryParams params) {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery()
                .active()
                .orderByStartTime()
                .desc();

        // 租户过滤
        String tenantId = TenantContext.getTenantId();
        if (StrUtil.isNotBlank(tenantId)) {
            query.processInstanceTenantId(tenantId);
        }

        long total = query.count();
        int current = (int) params.getCurrent();
        int pageSize = (int) params.getPageSize();
        int offset = (current - 1) * pageSize;
        List<ProcessInstance> list = query.listPage(offset, pageSize);
        return new PageResult<>(list, total);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void suspend(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activate(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void terminate(String processInstanceId, String reason) {
        // 终止流程实例
        runtimeService.deleteProcessInstance(processInstanceId, 
            "管理员终止: " + reason);
    }

    @Override
    public Map<String, Object> getTrace(String processInstanceId) {
        // 获取流程追踪信息
        List<HistoricActivityInstance> activities = historyService
            .createHistoricActivityInstanceQuery()
            .processInstanceId(processInstanceId)
            .orderByHistoricActivityInstanceStartTime()
            .asc()
            .list();
        
        Map<String, Object> trace = new HashMap<>();
        trace.put("activities", activities);
        return trace;
    }

    @Override
    public Map<String, Object> getVariables(String processInstanceId) {
        // 获取流程变量
        // TODO: 实现获取流程变量
        return Map.of();
    }

    // ====================== 私有辅助方法 ======================

    /**
     * 为单个流程实例构建含完整审批轨迹的 VO
     * <p>
     * nodeType 从 BPMN nodeConfig JSON 扩展元素中读取："0"→10(发起人)、"11"→13(办理人)
     * 读不到时回退到位置推断（首个 userTask=10，其余=13）
     *
     * @param instance  历史流程实例
     * @param bpmnCache 本次请求内复用的 BPMN FlowElement 缓存
     */
    private ProcessInstanceVO buildTraceVO(HistoricProcessInstance instance,
                                                     Map<String, Map<String, FlowElement>> bpmnCache) {
        String processInstanceId = instance.getId();
        ProcessInstanceVO vo = new ProcessInstanceVO();

        // 1. 基本信息
        vo.setProcessInstanceId(processInstanceId);
        vo.setProcessDefinitionId(instance.getProcessDefinitionId());
        vo.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        vo.setProcessDefinitionName(instance.getProcessDefinitionName());
        vo.setProcessInstanceName(instance.getName());
        vo.setBusinessKey(instance.getBusinessKey());
        vo.setStartUserId(instance.getStartUserId());
        vo.setStartTime(instance.getStartTime());
        vo.setEndTime(instance.getEndTime());
        vo.setDurationInMillis(instance.getDurationInMillis());
        vo.setStatus(instance.getEndTime() != null ? ProcessInstanceStateEnum.COMPLETED : ProcessInstanceStateEnum.RUNNING);

        // 2. 查询所有活动节点（按开始时间排序）
        List<HistoricActivityInstance> activities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        if (activities == null) {
            activities = Collections.emptyList();
        }

        // 3. 构建 审批意见 taskId → reason 映射
        Map<String, String> taskReasonMap = new HashMap<>();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        if (comments != null) {
            for (Comment comment : comments) {
                if (comment.getTaskId() != null) {
                    taskReasonMap.put(comment.getTaskId(), comment.getFullMessage());
                }
            }
        }

        // 4. 收集所有经办人用户ID（含发起人），批量查询用户信息
        Set<Long> userIdSet = new HashSet<>();
        // 发起人 ID
        if (instance.getStartUserId() != null) {
            try {
                userIdSet.add(Long.parseLong(instance.getStartUserId()));
            } catch (NumberFormatException ignored) {
            }
        }
        for (HistoricActivityInstance activity : activities) {
            if (activity.getAssignee() != null) {
                try {
                    userIdSet.add(Long.parseLong(activity.getAssignee()));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        Map<Long, SysUserVO> userMap = batchQueryUsers(userIdSet);

        // 发起人详细信息
        if (instance.getStartUserId() != null) {
            try {
                Long suid = Long.parseLong(instance.getStartUserId());
                SysUserVO startUser = userMap.get(suid);
                if (startUser != null) {
                    vo.setStartUserName(startUser.getRealName() != null ? startUser.getRealName() : startUser.getUsername());
                    vo.setStartDeptId(startUser.getSysDeptId() != null ? String.valueOf(startUser.getSysDeptId()) : null);
                    vo.setStartDeptName(startUser.getDeptName());
                }
            } catch (NumberFormatException ignored) {
            }
        }

        // 5. 查询运行时任务（获取活跃节点的 assignee + 候选人）
        Map<String, String> runtimeAssigneeMap = new HashMap<>();
        Map<String, List<String>> runtimeCandidatesMap = new HashMap<>();
        List<Task> runtimeTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (runtimeTasks != null) {
            for (Task rt : runtimeTasks) {
                String key = rt.getTaskDefinitionKey();
                if (key == null) continue;
                if (rt.getAssignee() != null) {
                    runtimeAssigneeMap.put(key, rt.getAssignee());
                }
                // 查询候选人
                List<IdentityLink> links = taskService.getIdentityLinksForTask(rt.getId());
                if (links != null) {
                    List<String> candidateIds = new ArrayList<>();
                    for (IdentityLink link : links) {
                        if (IdentityLinkType.CANDIDATE.equals(link.getType()) && link.getUserId() != null) {
                            candidateIds.add(link.getUserId());
                        }
                    }
                    if (!candidateIds.isEmpty()) {
                        runtimeCandidatesMap.put(key, candidateIds);
                    }
                }
            }
        }

        // 5. 从缓存获取 BPMN FlowElement 映射（同一定义只加载一次）
        Map<String, FlowElement> flowElementMap = bpmnCache.computeIfAbsent(
                instance.getProcessDefinitionId(), this::loadFlowElementMap);

        // 6. 组装 ActivityNodeVO 列表
        List<ActivityNodeVO> activityNodes = new ArrayList<>();
        int userTaskIndex = 0;

        for (HistoricActivityInstance activity : activities) {
            String activityType = activity.getActivityType();
            String activityId = activity.getActivityId();

            ActivityNodeVO node = new ActivityNodeVO();
            node.setId(activityId);
            node.setName(activity.getActivityName());
            node.setStartTime(activity.getStartTime() != null ? activity.getStartTime().getTime() : null);
            node.setEndTime(activity.getEndTime() != null ? activity.getEndTime().getTime() : null);
            node.setStatus(activity.getEndTime() != null ? 2 : 1);
            node.setProcessInstanceId(processInstanceId);
            node.setCandidateUsers(Collections.emptyList());

            if (FlowableActivityTypeEnum.END_EVENT.getValue().equals(activityType)) {
                // 结束节点
                node.setNodeType(1);
                node.setTasks(null);
                node.setCandidateStrategy(null);

            } else if (FlowableActivityTypeEnum.START_EVENT.getValue().equals(activityType)) {
                // 开始节点
                node.setNodeType(0);
                node.setTasks(null);
                node.setCandidateStrategy(null);

            } else if (FlowableActivityTypeEnum.USER_TASK.getValue().equals(activityType)) {
                userTaskIndex++;

                // 从 BPMN nodeConfig JSON 扩展元素中读取 nodeType
                // "0"=发起人→10, "11"=审批人→13, 读不到时回退位置推断
                Integer nodeType = readNodeTypeFromConfig(flowElementMap, activityId);
                if (nodeType != null) {
                    node.setNodeType(nodeType);
                } else {
                    node.setNodeType(userTaskIndex == 1 ? 10 : 13);
                }
                node.setCandidateStrategy(readCandidateStrategy(flowElementMap, activityId));

                // 构建审批任务
                ActivityNodeTaskVO task = new ActivityNodeTaskVO();
                task.setId(activity.getTaskId());
                task.setStatus(activity.getEndTime() != null ? 2 : 1);
                task.setReason(taskReasonMap.get(activity.getTaskId()));
                task.setSignPicUrl(null);

                // 审批人信息：优先 HistoricActivity.assignee → 运行时 Task.assignee →
                // 运行时候选人首位 → 发起人节点回退 startUserId
                String assigneeId = activity.getAssignee();
                List<String> candidateIds = null;
                if (activity.getEndTime() == null) {
                    // 活跃任务
                    if (assigneeId == null) {
                        assigneeId = runtimeAssigneeMap.get(activity.getActivityId());
                    }
                    candidateIds = runtimeCandidatesMap.get(activity.getActivityId());
                    // 候选人首位作为 assigneeUser 兜底
                    if (assigneeId == null && candidateIds != null && !candidateIds.isEmpty()) {
                        assigneeId = candidateIds.get(0);
                    }
                }
                if (assigneeId == null && Integer.valueOf(10).equals(node.getNodeType())) {
                    assigneeId = instance.getStartUserId();
                }
                if (assigneeId != null) {
                    try {
                        Long uid = Long.parseLong(assigneeId);
                        SysUserVO sysUser = userMap.get(uid);
                        if (sysUser == null) {
                            // 可能未在批量查询中，单独查
                            Map<Long, SysUserVO> single = batchQueryUsers(Set.of(uid));
                            sysUser = single.get(uid);
                        }
                        task.setAssigneeUser(AssigneeUserVO.from(sysUser));
                    } catch (NumberFormatException ignored) {
                    }
                }

                // 填充候选人列表
                if (candidateIds != null && !candidateIds.isEmpty()) {
                    List<AssigneeUserVO> candidateUsers = new ArrayList<>();
                    for (String cid : candidateIds) {
                        try {
                            Long cuid = Long.parseLong(cid);
                            SysUserVO su = userMap.get(cuid);
                            if (su == null) {
                                Map<Long, SysUserVO> single = batchQueryUsers(Set.of(cuid));
                                su = single.get(cuid);
                            }
                            if (su != null) {
                                candidateUsers.add(AssigneeUserVO.from(su));
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    node.setCandidateUsers(candidateUsers);
                }

                node.setTasks(Collections.singletonList(task));
            } else {
                // 跳过网关、序列流等非审批节点
                continue;
            }

            activityNodes.add(node);
        }

        // 流程未结束时，从 BPMN 模型补上结束节点
        if (instance.getEndTime() == null) {
            for (FlowElement fe : flowElementMap.values()) {
                if (fe instanceof EndEvent endEvent) {
                    ActivityNodeVO endNode = new ActivityNodeVO();
                    endNode.setId(endEvent.getId());
                    endNode.setName(endEvent.getName() != null ? endEvent.getName() : "结束");
                    endNode.setNodeType(1);
                    endNode.setStatus(1);
                    endNode.setStartTime(null);
                    endNode.setEndTime(null);
                    endNode.setTasks(null);
                    endNode.setCandidateStrategy(null);
                    endNode.setCandidateUsers(Collections.emptyList());
                    endNode.setProcessInstanceId(processInstanceId);
                    activityNodes.add(endNode);
                }
            }
        }

        // vo.setCurrentTasks(activityNodes);
        return vo;
    }

    /**
     * 加载流程定义的 FlowElement 映射（activityId → FlowElement）
     */
    private Map<String, FlowElement> loadFlowElementMap(String processDefinitionId) {
        try {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            if (bpmnModel == null || bpmnModel.getMainProcess() == null) {
                return Collections.emptyMap();
            }
            return bpmnModel.getMainProcess().getFlowElementMap();
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 从 BPMN UserTask 的 nodeConfig JSON 扩展元素中读取 nodeType
     * <p>
     * nodeConfig JSON 结构示例：
     * {"nodeType":"0", "settype":"1", ...}  // "0"=发起人, "11"=审批人
     *
     * @return 映射后的 nodeType（10=发起人, 13=办理人），null 表示未读到
     */
    private Integer readNodeTypeFromConfig(Map<String, FlowElement> flowElementMap, String activityId) {
        if (flowElementMap == null) {
            return null;
        }
        FlowElement element = flowElementMap.get(activityId);
        if (!(element instanceof UserTask userTask)) {
            return null;
        }
        Map<String, List<ExtensionElement>> extMap = userTask.getExtensionElements();
        if (extMap == null) {
            return null;
        }
        // 读取 nodeConfig 扩展元素（JSON 格式）
        List<ExtensionElement> configElements = extMap.get("nodeConfig");
        if (configElements == null || configElements.isEmpty()) {
            return null;
        }
        try {
            String jsonText = configElements.get(0).getElementText();
            cn.hutool.json.JSONObject cfg = new cn.hutool.json.JSONObject(jsonText);
            String nodeTypeStr = cfg.getStr("nodeType");
            if ("0".equals(nodeTypeStr)) {
                return 10;  // 发起人
            } else if ("11".equals(nodeTypeStr)) {
                return 13;  // 办理人
            }
            // 尝试解析为整数映射
            int raw = Integer.parseInt(nodeTypeStr);
            if (raw == 0) return 10;
            if (raw == 1) return 13;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 从 BPMN UserTask 扩展属性中读取 candidateStrategy
     */
    private Integer readCandidateStrategy(Map<String, FlowElement> flowElementMap, String activityId) {
        if (flowElementMap == null) {
            return null;
        }
        FlowElement element = flowElementMap.get(activityId);
        if (element instanceof UserTask userTask) {
            Map<String, List<ExtensionElement>> extMap = userTask.getExtensionElements();
            if (extMap != null) {
                List<ExtensionElement> strategyElements = extMap.get("candidateStrategy");
                if (strategyElements != null && !strategyElements.isEmpty()) {
                    try {
                        return Integer.parseInt(strategyElements.get(0).getElementText());
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取当前所在审批任务（流程未结束时返回活跃的审批节点，已结束返回 null）
     */
    private ActivityNodeVO getCurrentTask(HistoricProcessInstance instance) {
        if (instance.getEndTime() != null) {
            return null;
        }
        String processInstanceId = instance.getId();
        List<Task> runtimeTasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (runtimeTasks == null || runtimeTasks.isEmpty()) {
            return null;
        }
        // 取第一个活跃任务
        Task rt = runtimeTasks.get(0);
        ActivityNodeVO node = new ActivityNodeVO();
        node.setId(rt.getTaskDefinitionKey());
        node.setName(rt.getName());
        node.setNodeType(13);
        node.setStatus(1);
        node.setStartTime(rt.getCreateTime() != null ? rt.getCreateTime().getTime() : null);
        node.setEndTime(null);
        node.setProcessInstanceId(processInstanceId);
        node.setCandidateStrategy(null);

        // 审批任务
        ActivityNodeTaskVO task = new ActivityNodeTaskVO();
        task.setId(rt.getId());
        task.setStatus(1);
        task.setSignPicUrl(null);

        // 审批人
        String assigneeId = rt.getAssignee();
        List<String> candidateIds = new ArrayList<>();
        if (assigneeId == null) {
            List<IdentityLink> links = taskService.getIdentityLinksForTask(rt.getId());
            if (links != null) {
                for (IdentityLink link : links) {
                    if (IdentityLinkType.CANDIDATE.equals(link.getType()) && link.getUserId() != null) {
                        candidateIds.add(link.getUserId());
                    }
                }
            }
            if (!candidateIds.isEmpty()) {
                assigneeId = candidateIds.get(0);
            }
        }
        if (assigneeId != null) {
            try {
                Long uid = Long.parseLong(assigneeId);
                Map<Long, SysUserVO> userMap = batchQueryUsers(Set.of(uid));
                task.setAssigneeUser(AssigneeUserVO.from(userMap.get(uid)));
            } catch (NumberFormatException ignored) {
            }
        }

        // 候选人
        if (!candidateIds.isEmpty()) {
            List<AssigneeUserVO> candidateUsers = new ArrayList<>();
            Set<Long> cuids = candidateIds.stream().map(Long::parseLong).collect(Collectors.toSet());
            Map<Long, SysUserVO> cuMap = batchQueryUsers(cuids);
            for (String cid : candidateIds) {
                try {
                    SysUserVO su = cuMap.get(Long.parseLong(cid));
                    if (su != null) candidateUsers.add(AssigneeUserVO.from(su));
                } catch (NumberFormatException ignored) {
                }
            }
            node.setCandidateUsers(candidateUsers);
        } else {
            node.setCandidateUsers(Collections.emptyList());
        }

        node.setTasks(Collections.singletonList(task));
        return node;
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
            return Collections.emptyMap();
        }
    }
}
