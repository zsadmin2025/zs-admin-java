package com.zs.bpm.model.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zs.bpm.model.resolver.ApproverResolver;
import jakarta.annotation.Resource;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态审批人计算监听器
 * <p>
 * 在 UserTask 节点进入时触发，从 {@code <flowable:nodeConfig>} JSON 扩展元素中
 * 读取审批策略和参数，动态查询审批人列表，写入统一变量 {@code assigneeList}
 * 供多实例消费。
 * <p>
 * 不再依赖节点 ID 拼接变量名，不再逐字段读取分散的扩展元素。
 */
@Component("dynamicApproverCalcListener")
public class DynamicApproverCalcListener implements ExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(DynamicApproverCalcListener.class);

    private static final String FLOWABLE_NS = "http://flowable.org/bpmn";
    
    /** 统一变量名，不再拼接节点 ID，必须与 BPMN 多实例 collection 变量名一致 */
    private static final String APPROVER_VAR = "assigneeList";

    /** 默认兜底策略码，用于未匹配到策略时回退 */
    private static final int DEFAULT_RESOLVER_CODE = -1;

    @Resource
    private RuntimeService runtimeService;

    private final Map<Integer, ApproverResolver> resolverMap = new HashMap<>();

    @Autowired
    public void setResolvers(List<ApproverResolver> resolvers) {
        resolvers.forEach(r -> resolverMap.put(r.getResolverCode(), r));
    }

    @Override
    public void notify(DelegateExecution execution) {
        UserTask userTask = (UserTask) execution.getCurrentFlowElement();
        log.warn("=== DynamicApproverCalcListener 触发: nodeId={}, procInstId={} ===",
                userTask.getId(), execution.getProcessInstanceId());
        
        // 1. 从 nodeConfig JSON 中读取审批配置（新格式优先）
        JSONObject nodeConfig = getNodeConfig(userTask);
        Integer strategy = null;
        String param = null;
        if (nodeConfig != null) {
            strategy = nodeConfig.getInt("settype");
            param = getCandidateParam(nodeConfig);
        } else {
            // 兜底：回退读取独立扩展元素（兼容旧格式 candidateStrategy / candidateParam）
            String strategyStr = getExtText(userTask, "candidateStrategy");
            param = getExtText(userTask, "candidateParam");
            if (StrUtil.isBlank(strategyStr)) {
                log.warn("节点 {} 缺少 nodeConfig 且无 candidateStrategy 扩展元素，跳过审批人计算", userTask.getId());
                return;
            }
            try {
                strategy = Integer.parseInt(strategyStr);
            } catch (NumberFormatException e) {
                log.warn("节点 {} 的 candidateStrategy 为非数字: {}", userTask.getId(), strategyStr);
                runtimeService.setVariable(execution.getProcessInstanceId(), APPROVER_VAR, List.of());
                return;
            }
            log.warn("节点 {} 使用独立扩展元素兜底: strategy={}, param={}", userTask.getId(), strategy, param);
        }
        
        // 与流程启动变量、发起人节点 assignee 保持一致，统一使用 startUserId
        String initiator = execution.getVariable("startUserId", String.class);

        // 2. 策略模式动态解析审批人
        ApproverResolver resolver = resolverMap.getOrDefault(strategy, resolverMap.get(DEFAULT_RESOLVER_CODE));
        if (resolver == null) {
            log.error("节点 {} 无对应审批策略解析器: strategy={}", userTask.getId(), strategy);
            runtimeService.setVariable(execution.getProcessInstanceId(), APPROVER_VAR, List.of());
            return;
        }
        List<String> approverList = resolver.resolve(param, initiator);

        // 3. 审批人列表为空时的兜底策略（对应前端 noHanderAction 配置）
        if (CollUtil.isEmpty(approverList) && nodeConfig != null) {
            approverList = applyEmptyApproverFallback(execution, nodeConfig, userTask.getId());
        }

        // 4. 写入流程实例级别变量，确保多实例子 execution 可继承
        if (CollUtil.isEmpty(approverList)) {
            log.warn("节点 {} 审批人列表为空: strategy={}, param={}", userTask.getId(), strategy, param);
        }
        runtimeService.setVariable(execution.getProcessInstanceId(), APPROVER_VAR, approverList);
        log.warn("节点 {} 审批人计算完成: strategy={}, count={}", userTask.getId(), strategy,
                approverList != null ? approverList.size() : 0);
    }

    /**
     * 审批人为空时的兜底策略处理
     * <p>
     * 对应前端"审批人为空时"配置：
     * 1=自动通过 → 返回空列表，多实例自动跳过节点
     * 2=自动拒绝 → 直接终止流程
     * 3=指定人员 → 读取 backupUsers
     * 4=转交管理员 → 待实现
     * </p>
     *
     * @param execution  当前执行实例（用于终止流程）
     * @param nodeConfig 节点配置 JSON
     * @param nodeId     节点 ID（用于日志）
     * @return 兜底审批人列表
     */
    private List<String> applyEmptyApproverFallback(DelegateExecution execution, JSONObject nodeConfig, String nodeId) {
        Integer noHanderAction = nodeConfig.getInt("noHanderAction");
        if (noHanderAction == null) {
            return Collections.emptyList();
        }
        return switch (noHanderAction) {
            case 1 -> {
                // 自动通过：返回空列表，多实例 collection 为空直接跳过节点
                log.info("节点 {} 审批人为空，自动通过（跳过节点）", nodeId);
                yield Collections.emptyList();
            }
            case 2 -> {
                // 自动拒绝：直接终止流程，不再继续
                log.info("节点 {} 审批人为空，自动拒绝（终止流程）", nodeId);
                runtimeService.deleteProcessInstance(execution.getProcessInstanceId(),
                        "审批人为空，自动拒绝");
                yield Collections.emptyList();
            }
            case 3 -> {
                // 指定备用人员
                String backupUsers = nodeConfig.getStr("backupUsers");
                if (StrUtil.isNotBlank(backupUsers)) {
                    log.info("节点 {} 审批人为空，使用备用人员: {}", nodeId, backupUsers);
                    yield List.of(backupUsers.split(","));
                }
                log.warn("节点 {} 审批人为空且未配置备用人员", nodeId);
                yield Collections.emptyList();
            }
            case 4 -> {
                log.warn("节点 {} 审批人为空，转交管理员功能待实现", nodeId);
                yield Collections.emptyList();
            }
            default -> {
                log.warn("节点 {} 未知的审批人空值策略: {}", nodeId, noHanderAction);
                yield Collections.emptyList();
            }
        };
    }

    // ==================== 扩展字段解析 ====================
    
    /**
     * 从 nodeConfig 中提取 candidateParam，兼容 JSON 数组和字符串两种格式
     * <p>
     * 前端 JSON 模型传的是数组 ["2000000000000000004"]，
     * 直接用 getStr() 会拿到数组的 JSON 字符串表示而非实际值，导致 resolver 无法解析。
     * </p>
     *
     * @param nodeConfig 节点配置 JSON 对象
     * @return candidateParam 字符串值，不存在时返回 null
     */
    private String getCandidateParam(JSONObject nodeConfig) {
        Object obj = nodeConfig.get("candidateParam");
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONArray arr) {
            return arr.isEmpty() ? null : arr.getStr(0);
        }
        return obj.toString();
    }
    
    /**
     * 从 UserTask 扩展元素中读取 nodeConfig JSON
     * 优先读取 nodeConfig（BpmnXmlGenerator 生成的新格式），
     * 回退读取 approveConfig（FlowJsonToBpmnUtil 生成的旧格式）
     *
     * @param task UserTask 节点
     * @return nodeConfig JSON 对象，解析失败或不存在时返回 null
     */
    private JSONObject getNodeConfig(UserTask task) {
        // 优先读取 nodeConfig（BpmnXmlGenerator 生成的新格式）
        String configJson = getExtText(task, "nodeConfig");
        // 回退读取 approveConfig（FlowJsonToBpmnUtil 生成的旧格式）
        if (StrUtil.isBlank(configJson)) {
            configJson = getExtText(task, "approveConfig");
        }
        if (StrUtil.isBlank(configJson)) return null;
        try {
            return JSONUtil.parseObj(configJson);
        } catch (Exception e) {
            log.error("解析 nodeConfig/approveConfig JSON 失败: taskId={}", task.getId(), e);
            return null;
        }
    }

    /**
     * 从 UserTask 扩展元素中读取指定名称的文本内容
     *
     * @param task UserTask 节点
     * @param name 扩展元素名称
     * @return 扩展元素文本内容，不存在时返回空字符串
     */
    private String getExtText(UserTask task, String name) {
        Map<String, List<ExtensionElement>> extMap = task.getExtensionElements();
        if (extMap == null) return "";
        List<ExtensionElement> list = extMap.get(name);
        return CollUtil.isEmpty(list) ? "" : list.get(0).getElementText();
    }
}
