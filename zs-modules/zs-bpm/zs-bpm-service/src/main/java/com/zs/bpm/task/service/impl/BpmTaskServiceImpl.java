package com.zs.bpm.task.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zs.bpm.cc.domain.entity.BpmCcRecordEntity;
import com.zs.bpm.cc.domain.vo.BpmCcRecordVO;
import com.zs.bpm.cc.service.IBpmCcRecordService;
import com.zs.bpm.process.domain.vo.AssigneeUserVO;
import com.zs.bpm.process.service.IBpmProcessDetailService;
import com.zs.bpm.task.domain.params.*;
import com.zs.bpm.task.domain.vo.AllTaskVO;
import com.zs.bpm.task.domain.vo.FlowNode;
import com.zs.bpm.task.domain.vo.ProcessDetailVO;
import com.zs.bpm.task.domain.vo.ProcessInstanceInfo;
import com.zs.bpm.task.service.IBpmTaskService;
import com.zs.common.core.enums.BpmTaskActionEnum;
import com.zs.common.core.enums.bpmn.FlowNodeStatusEnum;
import com.zs.common.core.enums.bpmn.ProcessInstanceStateEnum;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.sys.user.domain.vo.SysUserVO;
import com.zs.sys.user.service.ISysUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BpmTaskServiceImpl implements IBpmTaskService {

    @Resource
    private IBpmProcessDetailService processDetailService;

    @Resource
    private TaskService taskService;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private IdentityService identityService;

    @Resource
    private ProcessEngine processEngine;

    @Resource
    private IBpmCcRecordService ccRecordService;

    @Lazy
    @Resource
    private ISysUserService sysUserService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startProcess(TaskProcessParams params) {

        String processDefinitionId = params.getProcessDefinitionId();
        String loginUserId = String.valueOf(SecurityUtil.getUserId());
        String realName = SecurityUtil.getRealName();
        String tenantId = TenantContext.getTenantId();

        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionId);
        if (processDefinition == null) {
            log.error("流程启动失败：未找到对应的流程定义，ID: {}，租户: {}", processDefinitionId, tenantId);
            throw new ZsException("未找到对应的流程定义，请检查 ID 是否正确");
        }

        String businessKey = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        String instanceName = String.format("%s发起的%s", realName, processDefinition.getName());

        // 拷贝外部传入变量
        Map<String, Object> variables = new HashMap<>();
        if (params.getVariables() != null) {
            variables.putAll(params.getVariables());
        }


        variables.put("startUserId", loginUserId); // 匹配BPMN 发起人节点 assignee 表达式
        variables.put("formDataJson", JSONUtil.toJsonStr(params.getVariables()));
        log.info("启动流程变量集合：{}", variables);
        try {
            identityService.setAuthenticatedUserId(loginUserId);

            ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
                    .processDefinitionId(processDefinitionId) // 流程定义ID
                    .businessKey(businessKey) // 设置业务标识
                    .name(instanceName)       // 设置流程实例的名称
                    .variables(variables)     // 设置流程变量
                    .owner(loginUserId)       // 设置流程实例的拥有者
                    .tenantId(tenantId)
                    .start();

            log.info("流程启动成功，流程实例ID: {}", processInstance.getId());

            // 手动完成发起人节点：查询运行时任务 → 添加审批意见 → 完成任务
            Task initiatorTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .taskTenantId(tenantId)
                    .singleResult();
            if (initiatorTask != null) {
                taskService.addComment(initiatorTask.getId(), processInstance.getId(),
                        BpmTaskActionEnum.COMPLETE.getValue(), "发起人节点自动审批通过。");
                taskService.complete(initiatorTask.getId(), params.getVariables());
                log.info("发起人节点已自动完成，任务ID: {}", initiatorTask.getId());
            } else {
                log.warn("未找到发起人运行时任务，流程可能已直接进入下一节点");
            }
            return "流程启动成功" + processInstance.getId();
        } catch (Exception e) {
            log.error("启动流程发生异常，准备回滚. 原因: {}", e.getMessage(), e);
            throw new ZsException("流程启动并提交失败: " + e.getMessage());
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    @Override
    public ProcessDetailVO getProcessDetail(TodoTaskParams params) {
        return processDetailService.getProcessDetail(params);
    }

    @Override
    public ProcessDetailVO getProcessDetailByBusinessKey(String businessKey) {
        HistoricProcessInstance processInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (processInstance == null) {
            throw new ZsException("未找到业务主键对应的流程实例: " + businessKey);
        }
        return null;
    }


    @Override
    public PageResult<ProcessInstanceInfo> todoPage(TaskPageQueryParams params) {
        String userId = String.valueOf(SecurityUtil.getUserId());
        String tenantId = TenantContext.getTenantId();

        // 1. 查询当前用户所有待办任务（assignee + candidate），提取去重流程实例ID
        List<Task> allUserTasks = taskService.createTaskQuery()
                .active()
                .or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .endOr()
                .taskTenantId(tenantId)
                .list();

        if (CollUtil.isEmpty(allUserTasks)) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        // 2. 按流程实例分组，后续组装时可直接取该实例下用户的待办任务列表
        Map<String, List<Task>> tasksByProcInst = allUserTasks.stream()
                .collect(Collectors.groupingBy(Task::getProcessInstanceId));

        // 3. 按流程实例维度分页查询
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(tasksByProcInst.keySet())
                .orderByProcessInstanceStartTime()
                .desc();

        if (StrUtil.isNotBlank(params.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + params.getProcessDefinitionName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessDefinitionKey())) {
            query.processDefinitionKeyLike("%" + params.getProcessDefinitionKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getBusinessKey())) {
            query.processInstanceBusinessKeyLike("%" + params.getBusinessKey() + "%");
        }

        long total = query.count();
        int offset = ((int) params.getCurrent() - 1) * (int) params.getPageSize();
        List<HistoricProcessInstance> instances = query.listPage(offset, (int) params.getPageSize());

        // 4. 批量加载发起人信息并组装结果
        Map<Long, SysUserVO> startUserMap = batchQueryUsers(extractStartUserIds(instances));

        List<ProcessInstanceInfo> result = instances.stream()
                .map(hpi -> buildTodoProcessInstanceInfo(hpi, tasksByProcInst.get(hpi.getId()), startUserMap))
                .collect(Collectors.toList());

        return new PageResult<>(result, total, ProcessInstanceInfo.class);
    }

    // ==================== 共享构建方法 ====================

    /**
     * 构建流程实例基础信息（流程属性 + 状态 + 发起人）
     */
    private ProcessInstanceInfo buildBaseProcessInstanceInfo(HistoricProcessInstance hpi,
                                                              Map<Long, SysUserVO> startUserMap) {
        ProcessInstanceInfo info = new ProcessInstanceInfo();
        info.setProcessInstanceId(hpi.getId());
        info.setProcessDefinitionId(hpi.getProcessDefinitionId());
        info.setProcessDefinitionName(hpi.getProcessDefinitionName());
        info.setProcessDefinitionKey(hpi.getProcessDefinitionKey());
        info.setProcessInstanceName(hpi.getName());
        info.setBusinessKey(hpi.getBusinessKey());
        info.setStartTime(hpi.getStartTime());
        info.setEndTime(hpi.getEndTime());
        info.setDurationInMillis(hpi.getDurationInMillis());
        info.setProcessState(resolveProcessState(hpi));
        setStartUserIfPresent(info, hpi.getStartUserId(), startUserMap);
        return info;
    }

    private String resolveProcessState(HistoricProcessInstance hpi) {
        if (hpi.getEndTime() == null) {
            return ProcessInstanceStateEnum.RUNNING.getValue();
        }
        if (StrUtil.isNotBlank(hpi.getDeleteReason())) {
            return ProcessInstanceStateEnum.CANCELLED.getValue();
        }
        return ProcessInstanceStateEnum.COMPLETED.getValue();
    }

    private Long parseUserId(String userIdStr) {
        if (StrUtil.isBlank(userIdStr)) return null;
        try { return Long.parseLong(userIdStr); } catch (NumberFormatException e) { return null; }
    }

    private Set<Long> extractStartUserIds(List<HistoricProcessInstance> instances) {
        return instances.stream()
                .map(HistoricProcessInstance::getStartUserId)
                .map(this::parseUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private void setStartUserIfPresent(ProcessInstanceInfo info, String startUserId,
                                        Map<Long, SysUserVO> startUserMap) {
        Long uid = parseUserId(startUserId);
        if (uid != null) {
            SysUserVO user = startUserMap.get(uid);
            if (user != null) {
                info.setStartUser(AssigneeUserVO.from(user));
            }
        }
    }

    // ==================== 待办 / 已办 ====================

    /**
     * 构建待办流程实例信息（含发起人和该实例下的待办任务概览）
     */
    private ProcessInstanceInfo buildTodoProcessInstanceInfo(HistoricProcessInstance hpi,
                                                             List<Task> userTasks,
                                                             Map<Long, SysUserVO> startUserMap) {
        ProcessInstanceInfo info = buildBaseProcessInstanceInfo(hpi, startUserMap);
        if (CollUtil.isNotEmpty(userTasks)) {
            info.setTodoTask(buildTodoFlowNode(hpi, userTasks.get(0)));
        }
        return info;
    }

    private FlowNode buildTodoFlowNode(HistoricProcessInstance hpi, Task task) {
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(hpi.getId());
        node.setProcessDefinitionId(hpi.getProcessDefinitionId());
        node.setProcessDefinitionName(hpi.getProcessDefinitionName());
        node.setProcessDefinitionKey(hpi.getProcessDefinitionKey());
        node.setTaskId(task.getId());
        node.setNodeKey(task.getTaskDefinitionKey());
        node.setNodeName(task.getName());
        node.setDescription(task.getDescription());
        node.setStartTime(task.getCreateTime());
        node.setStatus(FlowNodeStatusEnum.IN_PROGRESS.getValue());
        return node;
    }

    private FlowNode buildFinishedFlowNode(HistoricProcessInstance hpi, HistoricTaskInstance task) {
        FlowNode node = new FlowNode();
        node.setProcessInstanceId(hpi.getId());
        node.setProcessDefinitionId(hpi.getProcessDefinitionId());
        node.setProcessDefinitionKey(hpi.getProcessDefinitionKey());
        node.setProcessDefinitionName(hpi.getProcessDefinitionName());
        node.setTaskId(task.getId());
        node.setNodeKey(task.getTaskDefinitionKey());
        node.setNodeName(task.getName());
        node.setDescription(task.getDescription());
        node.setStartTime(task.getCreateTime());
        node.setEndTime(task.getEndTime());
        node.setDurationInMillis(task.getDurationInMillis());
        node.setStatus(FlowNodeStatusEnum.COMPLETED.getValue());
        return node;
    }

    @Override
    public PageResult<ProcessInstanceInfo> finishedPage(TaskPageQueryParams params) {
        String userId = String.valueOf(SecurityUtil.getUserId());
        String tenantId = TenantContext.getTenantId();

        // 1. 构建已办任务查询（assignee + candidate 双通道），直接在任务维度分页
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .endOr()
                .taskTenantId(tenantId)
                .orderByHistoricTaskInstanceEndTime()
                .desc();

        // 2. 应用流程实例维度的过滤条件
        if (StrUtil.isNotBlank(params.getProcessDefinitionName())) {
            taskQuery.processDefinitionNameLike("%" + params.getProcessDefinitionName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessDefinitionKey())) {
            taskQuery.processDefinitionKeyLike("%" + params.getProcessDefinitionKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getBusinessKey())) {
            taskQuery.processInstanceBusinessKeyLike("%" + params.getBusinessKey() + "%");
        }

        // 3. 任务级分页
        long total = taskQuery.count();
        if (total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        int offset = ((int) params.getCurrent() - 1) * (int) params.getPageSize();
        List<HistoricTaskInstance> tasks = taskQuery.listPage(offset, (int) params.getPageSize());

        // 4. 批量加载流程实例信息
        Set<String> procInstIds = tasks.stream()
                .map(HistoricTaskInstance::getProcessInstanceId)
                .collect(Collectors.toSet());
        List<HistoricProcessInstance> procInstances = historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(procInstIds)
                .list();
        Map<String, HistoricProcessInstance> procInstMap = procInstances.stream()
                .collect(Collectors.toMap(HistoricProcessInstance::getId, hpi -> hpi));

        // 5. 批量加载发起人信息并组装结果（每条已办任务 = 一行）
        Map<Long, SysUserVO> startUserMap = batchQueryUsers(extractStartUserIds(procInstances));

        List<ProcessInstanceInfo> result = tasks.stream()
                .map(task -> {
                    HistoricProcessInstance hpi = procInstMap.get(task.getProcessInstanceId());
                    ProcessInstanceInfo info = buildBaseProcessInstanceInfo(hpi, startUserMap);
                    info.setTodoTask(buildFinishedFlowNode(hpi, task));
                    return info;
                })
                .collect(Collectors.toList());

        return new PageResult<>(result, total, ProcessInstanceInfo.class);
    }



    @Override
    public PageResult<BpmCcRecordVO> ccPage(TaskPageQueryParams params) {
        Long userId = SecurityUtil.getUserId();
        if (userId == null) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        int current = (int) params.getCurrent();
        int pageSize = (int) params.getPageSize();

        // 1. 解析流程过滤条件，从 Flowable 获取匹配的 processInstanceIds
        Set<String> filterProcInstIds = null;
        if (hasProcessFilter(params)) {
            filterProcInstIds = resolveFilteredProcessInstanceIds(params);
            if (filterProcInstIds.isEmpty()) {
                return new PageResult<>(Collections.emptyList(), 0L);
            }
        }

        // 2. 从 bpm_cc_record 表分页查询抄送记录
        PageResult<BpmCcRecordEntity> ccPageResult = ccRecordService.pageCcList(userId, filterProcInstIds, current, pageSize);
        List<BpmCcRecordEntity> ccRecords = ccPageResult.getList();
        if (CollUtil.isEmpty(ccRecords)) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        // 3. 收集流程实例ID，批量加载流程实例信息
        Set<String> procInstIds = ccRecords.stream()
                .map(BpmCcRecordEntity::getProcessInstanceId)
                .collect(Collectors.toSet());
        List<HistoricProcessInstance> procInstances = historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(procInstIds)
                .list();
        Map<String, HistoricProcessInstance> procInstMap = procInstances.stream()
                .collect(Collectors.toMap(HistoricProcessInstance::getId, hpi -> hpi, (a, b) -> a));

        // 4. 批量加载发起人用户信息
        Map<Long, SysUserVO> startUserMap = batchQueryUsers(extractStartUserIds(procInstances));

        // 5. 批量加载抄送发起人用户信息（bpm_cc_record.cc_sender_id 字段）
        Set<Long> senderIds = ccRecords.stream()
                .map(BpmCcRecordEntity::getCcSenderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, SysUserVO> senderUserMap = batchQueryUsers(senderIds);

        // 6. 组装 VO
        List<BpmCcRecordVO> voList = ccRecords.stream()
                .map(record -> buildCcRecordVO(record, procInstMap, startUserMap, senderUserMap))
                .collect(Collectors.toList());

        return new PageResult<>(voList, ccPageResult.getTotal());
    }

    /**
     * 判断是否有流程维度的过滤条件
     */
    private boolean hasProcessFilter(TaskPageQueryParams params) {
        return StrUtil.isNotBlank(params.getProcessDefinitionName())
                || StrUtil.isNotBlank(params.getProcessDefinitionKey())
                || StrUtil.isNotBlank(params.getBusinessKey());
    }

    /**
     * 从 Flowable 查询匹配过滤条件的流程实例ID集合
     */
    private Set<String> resolveFilteredProcessInstanceIds(TaskPageQueryParams params) {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StrUtil.isNotBlank(params.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + params.getProcessDefinitionName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessInstanceName())) {
            query.processInstanceNameLike("%" + params.getProcessInstanceName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessDefinitionKey())) {
            query.processDefinitionKeyLike("%" + params.getProcessDefinitionKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getBusinessKey())) {
            query.processInstanceBusinessKeyLike("%" + params.getBusinessKey() + "%");
        }
        return query.list().stream()
                .map(HistoricProcessInstance::getId)
                .collect(Collectors.toSet());
    }

    /**
     * 将抄送记录实体 + 流程实例信息组装为 VO
     */
    private BpmCcRecordVO buildCcRecordVO(BpmCcRecordEntity record,
                                           Map<String, HistoricProcessInstance> procInstMap,
                                           Map<Long, SysUserVO> startUserMap,
                                           Map<Long, SysUserVO> senderUserMap) {
        BpmCcRecordVO vo = new BpmCcRecordVO();
        vo.setId(record.getId());
        vo.setProcessInstanceId(record.getProcessInstanceId());
        vo.setTaskId(record.getTaskId());
        vo.setUserId(record.getUserId());
        vo.setTitle(record.getTitle());
        vo.setIsRead(record.getIsRead());
        vo.setReadTime(record.getReadTime());
        vo.setCreateTime(record.getCreateTime());

        // 填充抄送发起人信息（谁抄送给我的）
        Long senderId = record.getCcSenderId();
        if (senderId != null && senderId > 0) {
            vo.setCcSenderId(senderId);
            SysUserVO sender = senderUserMap.get(senderId);
            if (sender != null) {
                vo.setCcSenderName(sender.getRealName());
            }
        }
        vo.setCcType(record.getCcType());

        // 填充流程实例信息
        HistoricProcessInstance hpi = procInstMap.get(record.getProcessInstanceId());
        if (hpi != null) {
            vo.setProcessDefinitionName(hpi.getProcessDefinitionName());
            vo.setProcessDefinitionKey(hpi.getProcessDefinitionKey());
            vo.setBusinessKey(hpi.getBusinessKey());
            vo.setProcessInstanceName(hpi.getName());
            vo.setStartTime(hpi.getStartTime());
            vo.setEndTime(hpi.getEndTime());
            vo.setProcessState(resolveProcessState(hpi));

            Long startUid = parseUserId(hpi.getStartUserId());
            if (startUid != null) {
                vo.setStartUserId(startUid);
                SysUserVO user = startUserMap.get(startUid);
                if (user != null) {
                    vo.setStartUserName(user.getRealName());
                }
            }
        }
        return vo;
    }


    @Override
    public PageResult<Task> getAllActiveTasks(TaskPageQueryParams params) {
        TaskQuery query = taskService.createTaskQuery()
                .active()
                .orderByTaskCreateTime()
                .desc();
        if (StrUtil.isNotBlank(params.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + params.getProcessDefinitionName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessDefinitionKey())) {
            query.processDefinitionKeyLike("%" + params.getProcessDefinitionKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getBusinessKey())) {
            query.processInstanceBusinessKeyLike("%" + params.getBusinessKey() + "%");
        }
        int current = (int) params.getCurrent();
        int pageSize = (int) params.getPageSize();
        // 计算正确偏移量
        int offset = (current - 1) * pageSize;
        List<Task> list = query.listPage(offset, pageSize);
        return new PageResult<>(list, list.size(), Task.class);
    }

    // ==================== 全部任务查询（待办 + 已办） ====================

    @Override
    public PageResult<AllTaskVO> allTaskPage(AllTaskPageQueryParams params) {
        String tenantId = TenantContext.getTenantId();
        int current = (int) params.getCurrent();
        int pageSize = (int) params.getPageSize();
        int offset = (current - 1) * pageSize;

        // 1. 历史任务表含全部任务（运行中 end_time_=null，无需双源合并）
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(tenantId)
                .orderByHistoricTaskInstanceStartTime().desc();
        if (StrUtil.isNotBlank(params.getProcessDefinitionName())) {
            query.processDefinitionNameLike("%" + params.getProcessDefinitionName() + "%");
        }
        if (StrUtil.isNotBlank(params.getProcessDefinitionKey())) {
            query.processDefinitionKeyLike("%" + params.getProcessDefinitionKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getBusinessKey())) {
            query.processInstanceBusinessKeyLike("%" + params.getBusinessKey() + "%");
        }
        if (StrUtil.isNotBlank(params.getTaskName())) {
            query.taskNameLike("%" + params.getTaskName() + "%");
        }
        if (params.getAssigneeId() != null) {
            query.taskAssignee(String.valueOf(params.getAssigneeId()));
        }

        long total = query.count();
        if (total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        List<HistoricTaskInstance> tasks = query.listPage(offset, pageSize);

        // 2. 批量加载流程实例
        Set<String> procInstIds = tasks.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet());
        List<HistoricProcessInstance> procInstances = historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(procInstIds).list();
        Map<String, HistoricProcessInstance> procInstMap = procInstances.stream()
                .collect(Collectors.toMap(HistoricProcessInstance::getId, hpi -> hpi, (a, b) -> a));

        // 3. 批量加载用户信息（发起人 + 审批人）
        Set<Long> userIds = new HashSet<>();
        procInstances.forEach(hpi -> {
            Long uid = parseUserId(hpi.getStartUserId());
            if (uid != null) userIds.add(uid);
        });
        tasks.forEach(t -> {
            Long uid = parseUserId(t.getAssignee());
            if (uid != null) userIds.add(uid);
        });
        Map<Long, SysUserVO> userMap = batchQueryUsers(userIds);

        // 4. 批量加载审批意见
        Map<String, String> commentByTaskId = new HashMap<>();
        for (String pid : procInstIds) {
            List<Comment> comments = taskService.getProcessInstanceComments(pid);
            if (CollUtil.isNotEmpty(comments)) {
                comments.stream().filter(c -> c.getTaskId() != null && StrUtil.isNotBlank(c.getFullMessage()))
                        .forEach(c -> commentByTaskId.merge(c.getTaskId(), c.getFullMessage(), (a, b) -> a + "；" + b));
            }
        }

        // 5. 组装 VO
        List<AllTaskVO> result = tasks.stream().map(t -> {
            AllTaskVO vo = new AllTaskVO();
            vo.setTaskId(t.getId());
            vo.setTaskName(t.getName());
            vo.setTaskDefinitionKey(t.getTaskDefinitionKey());
            vo.setTaskStartTime(t.getCreateTime());
            vo.setTaskEndTime(t.getEndTime());
            vo.setDurationInMillis(t.getDurationInMillis());
            vo.setApprovalStatus(t.getEndTime() != null ?
                    (StrUtil.isNotBlank(t.getDeleteReason()) ? "CANCELLED" : "COMPLETED") : "RUNNING");
            vo.setComment(commentByTaskId.get(t.getId()));
            fillAssignee(vo, t.getAssignee(), userMap);

            HistoricProcessInstance hpi = procInstMap.get(t.getProcessInstanceId());
            if (hpi != null) {
                vo.setProcessInstanceId(hpi.getId());
                vo.setProcessDefinitionId(hpi.getProcessDefinitionId());
                vo.setProcessDefinitionKey(hpi.getProcessDefinitionKey());
                vo.setProcessDefinitionName(hpi.getProcessDefinitionName());
                vo.setProcessInstanceName(hpi.getName());
                vo.setBusinessKey(hpi.getBusinessKey());
                vo.setProcessStartTime(hpi.getStartTime());
                vo.setProcessEndTime(hpi.getEndTime());
                vo.setProcessState(resolveProcessState(hpi));
                fillStartUser(vo, hpi.getStartUserId(), userMap);
            }
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(result, total);
    }

    private void fillAssignee(AllTaskVO vo, String userIdStr, Map<Long, SysUserVO> userMap) {
        Long uid = parseUserId(userIdStr);
        if (uid == null) return;
        vo.setAssigneeId(uid);
        SysUserVO u = userMap.get(uid);
        if (u != null) vo.setAssigneeName(u.getRealName());
    }

    private void fillStartUser(AllTaskVO vo, String userIdStr, Map<Long, SysUserVO> userMap) {
        Long uid = parseUserId(userIdStr);
        if (uid == null) return;
        vo.setStartUserId(uid);
        SysUserVO u = userMap.get(uid);
        if (u != null) vo.setStartUserName(u.getRealName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(TaskCompleteParams params) {

        String taskId = params.getTaskId(); // 任务ID
        String processInstanceId = params.getProcessInstanceId(); // 流程实例ID
        BpmTaskActionEnum action = params.getAction(); // 审批动作

        switch (action) {
            case COMPLETE:
                doApprove(taskId, processInstanceId, params);
                break;
            case REJECT:
                doReject(taskId, processInstanceId, params);
                break;
            default:
                throw new ZsException("不支持的审批动作: " + action);
        }
    }

    private void doApprove(String taskId, String processInstanceId, TaskCompleteParams params) {
        taskService.addComment(taskId, processInstanceId, BpmTaskActionEnum.COMPLETE.getValue(), params.getComment());
        taskService.complete(taskId, String.valueOf(SecurityUtil.getUserId()));
    }

    private void doReject(String taskId, String processInstanceId, TaskCompleteParams params) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        String rejectTarget = params.getRejectTarget();
        switch (rejectTarget) {
            case "INITIATOR":
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdTo(task.getTaskDefinitionKey(), "starter")
                        .changeState();
                break;
            case "PREV":
                String prevActivityId = getPreviousActivityId(task.getProcessInstanceId());
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdTo(task.getTaskDefinitionKey(), prevActivityId)
                        .changeState();
                break;
            case "ANY":
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdTo(task.getTaskDefinitionKey(), params.getRejectTargetActivityId())
                        .changeState();
                break;
        }
        taskService.addComment(taskId, processInstanceId, BpmTaskActionEnum.REJECT.getValue(), params.getComment());
        taskService.complete(taskId, String.valueOf(SecurityUtil.getUserId()));
    }

    private String getPreviousActivityId(String processInstanceId) {
        // 查询历史活动实例，获取上一个已完成的节点
        List<HistoricActivityInstance> activities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .desc()
                .list();

        if (activities != null && !activities.isEmpty()) {
            return activities.get(0).getActivityId();
        }
        throw new ZsException("无法获取上一节点");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String taskId, String userId) {
        // 1. 获取当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ZsException("任务不存在");
        }
        // 2. 转办（修改负责人）
        taskService.setAssignee(taskId, userId);
        // 3. 记录转办历史（通过评论）
        taskService.addComment(taskId, task.getProcessInstanceId(),
                "转办给用户: " + userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(String taskId, String userId) {
        // 1. 获取当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ZsException("任务不存在");
        }
        // 2. 委派任务
        taskService.delegateTask(taskId, userId);
        // 3. 记录委派历史
        taskService.addComment(taskId, task.getProcessInstanceId(),
                "委派给用户: " + userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolve(String taskId) {
        // 委派归还
        taskService.resolveTask(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSign(String taskId, List<String> userIds, boolean sequential) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ZsException("任务不存在");
        }

        String processInstanceId = task.getProcessInstanceId();
        String activityId = task.getTaskDefinitionKey();

        // 创建多实例执行
        for (String userId : userIds) {
            Map<String, Object> variables = new HashMap<>();
            variables.put("assignee", userId);

            // 动态添加执行
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(processInstanceId)
                    .moveActivityIdTo(activityId, activityId)
                    .processVariables(variables)
                    .changeState();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSign(String taskId, String executionId) {
        // 删除指定任务
        taskService.deleteTask(taskId, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(String processInstanceId, String reason) {
        // 1. 添加撤回意见
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        for (Task task : tasks) {
            taskService.addComment(task.getId(), processInstanceId,
                    "撤回原因: " + reason);
        }

        // 2. 删除流程实例
        runtimeService.deleteProcessInstance(processInstanceId, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendCc(String processInstanceId, String taskId, List<Long> ccUserIds, String title) {
        // 1. 获取流程实例信息
        if (processInstanceId == null && taskId != null) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            processInstanceId = task.getProcessInstanceId();
        }

        // 2. 创建抄送记录
        Long senderId = SecurityUtil.getUserId();
        for (Long userId : ccUserIds) {
            BpmCcRecordEntity record = new BpmCcRecordEntity();
            record.setProcessInstanceId(processInstanceId);
            record.setTaskId(taskId);
            record.setUserId(userId);
            record.setTitle(title);
            record.setIsRead(0);  // 未读
            record.setCcSenderId(senderId);
            record.setCcType(2);  // 手动抄送
            ccRecordService.save(record);
        }
    }

    @Override
    public void getProcessDiagram(String processInstanceId, HttpServletResponse response) {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();


        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream in = diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                activityIds,
                flows,
                engconf.getActivityFontName(),
                engconf.getLabelFontName(),
                engconf.getAnnotationFontName(),
                engconf.getClassLoader(),
                1.0,
                true);
        OutputStream out = null;
        byte[] buf = new byte[1024];
        int legth = 0;
        try {
            out = response.getOutputStream();
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        } catch (Exception e) {
            log.error("获取流程图发生异常. 原因: {}", e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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

}