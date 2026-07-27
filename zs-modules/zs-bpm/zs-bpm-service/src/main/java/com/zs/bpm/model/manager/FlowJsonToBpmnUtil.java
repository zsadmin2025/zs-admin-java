package com.zs.bpm.model.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 前端流程设计器JSON转BPMN XML工具类
 * <p>
 * 将钉钉设计器JSON格式的流程定义转换为BPMN 2.0标准XML。
 * <p>
 * 节点类型映射：
 * <ul>
 *   <li>type=0 发起人 → UserTask + skipExpression（自动通过）</li>
 *   <li>type=1 审批人 → UserTask + multiInstanceLoopCharacteristics + extensionElements</li>
 *   <li>type=2 抄送人 → ServiceTask + delegateExpression</li>
 *   <li>type=3 条件   → 条件分支SequenceFlow</li>
 *   <li>type=4 路由   → ExclusiveGateway（仅入口，无合并网关）</li>
 * </ul>
 * <p>
 * 扩展配置存储在 extensionElements 子元素中（CDATA包裹JSON）：
 * <ul>
 *   <li>approveConfig: 审批/抄送选人完整JSON</li>
 *   <li>fieldPermission: 表单字段权限JSON数组</li>
 *   <li>buttonSetting: 操作按钮权限JSON数组</li>
 * </ul>
 * <p>
 * 使用 BpmnAutoLayout 自动计算节点布局，运行时由 BusinessAssigneeLoader 动态解析审批人。
 *
 * @author zsadmin
 */
@Slf4j
public class FlowJsonToBpmnUtil {

    /** Flowable扩展命名空间 */
    private static final String FLOWABLE_NS = "http://flowable.org/bpmn";
    
    /** humanPerformer表达式 */
    private static final String HUMAN_PERFORMER_EXPRESSION = "${businessAssigneeLoader.loadAssigneeList(task)}";

    /**
     * 将前端JSON流程定义转换为BPMN XML字符串（核心方法1）
     *
     * @param flowJson    前端JSON格式的流程定义
     * @param processKey  流程唯一标识
     * @param processName 流程名称
     * @return BPMN 2.0 XML字符串
     */
    public static String jsonToBpmnXml(String flowJson, String processKey, String processName) {
        if (StrUtil.isBlank(flowJson)) {
            throw new IllegalArgumentException("流程JSON不能为空");
        }

        // 重置ID计数器
        idCounter = 0;

        JSONObject root = JSONUtil.parseObj(flowJson);
        JSONObject nodeConfig = root.getJSONObject("nodeConfig");
        if (nodeConfig == null) {
            throw new IllegalArgumentException("流程JSON缺少nodeConfig节点");
        }

        // 解析为内部节点树
        Node rootNode = parseNode(nodeConfig);

        // 构建BpmnModel
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.setTargetNamespace("http://www.flowable.org/processdef");

        Process process = new Process();
        process.setId(processKey);
        process.setName(processName);
        process.setExecutable(true);
        bpmnModel.addProcess(process);

        // 1. 创建开始事件
        StartEvent startEvent = new StartEvent();
        startEvent.setId("StartEvent");
        startEvent.setName("开始");
        process.addFlowElement(startEvent);

        // 2. 递归转换流程节点，返回（首个元素，末尾出口元素列表）
        ConvertResult result = convertNode(rootNode, process, processKey);

        // 3. 开始事件连接到流程首个元素
        if (result != null && result.firstElement != null) {
            connectFlow(process, startEvent.getId(), result.firstElement.getId(), null);
        }

        // 4. 所有末尾出口连接到结束事件
        EndEvent endEvent = new EndEvent();
        endEvent.setId("EndEvent");
        endEvent.setName("结束");
        process.addFlowElement(endEvent);

        if (result != null && result.exitElements != null) {
            for (FlowElement exit : result.exitElements) {
                connectFlow(process, exit.getId(), endEvent.getId(), null);
            }
        }

        // 5. 使用BpmnAutoLayout自动布局
        try {
            new BpmnAutoLayout(bpmnModel).execute();
        } catch (Exception e) {
            log.warn("自动布局失败，使用默认布局", e);
        }

        // 6. 生成BPMN XML（包含布局信息）
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] xmlBytes = xmlConverter.convertToXML(bpmnModel);

        return new String(xmlBytes, StandardCharsets.UTF_8);
    }

    /**
     * 将BPMN模型转换为前端JSON格式（核心方法2）
     *
     * @param bpmnModel BpmnModel对象
     * @return 前端JSON格式的流程定义
     */
    public static JSONObject bpmnToFlowJson(BpmnModel bpmnModel) {
        if (bpmnModel == null || bpmnModel.getMainProcess() == null) {
            return new JSONObject();
        }

        Process process = bpmnModel.getMainProcess();
        JSONObject result = new JSONObject();

        // 查找开始事件
        StartEvent startEvent = null;
        for (FlowElement element : process.getFlowElements()) {
            if (element instanceof StartEvent) {
                startEvent = (StartEvent) element;
                break;
            }
        }

        if (startEvent == null) {
            log.warn("BpmnModel中未找到开始事件");
            return result;
        }

        // 递归转换节点树
        JSONObject nodeConfig = convertFlowElementToJson(startEvent, process);
        result.set("nodeConfig", nodeConfig);

        return result;
    }

    // ==================== 递归转换 ====================

    /**
     * 递归转换节点，返回首个元素和所有末尾出口元素
     *
     * @param node    当前节点
     * @param process 流程对象
     * @param prefix  ID前缀（用于保证唯一性）
     * @return 转换结果（首个元素 + 末尾出口列表）
     */
    private static ConvertResult convertNode(Node node, Process process, String prefix) {
        if (node == null) {
            return null;
        }

        int nodeType = node.type;
        return switch (nodeType) {
            case 0 -> // 发起人节点，跳过，直接处理子节点
                    convertStartNode(node, process, prefix);
            case 1 -> // 审批节点
                    convertApprovalNode(node, process, prefix);
            case 2 -> // 抄送节点
                    convertCcNode(node, process, prefix);
            case 3 -> // 条件分支
                    convertConditionNode(node, process, prefix);
            case 4 -> // 路由节点
                    convertRouterNode(node, process, prefix);
            default -> {
                log.warn("未处理的节点类型: {}", nodeType);
                yield null;
            }
        };
    }

    /**
     * 转换发起人节点（type=0）
     * <p>
     * 生成UserTask作为发起人节点，配置skipExpression使其自动通过：
     * <ul>
     *   <li>assignee设为${startUserId}（流程启动时传入）</li>
     *   <li>skipExpression="${_SKIP_INITIATOR == true}"（流程启动时设置_SKIP_INITIATOR=true自动跳过）</li>
     * </ul>
     */
    private static ConvertResult convertStartNode(Node node, Process process, String prefix) {
        String taskId = generateId(prefix, "starter", node.name);

        UserTask userTask = new UserTask();
        userTask.setId(taskId);
        userTask.setName(StrUtil.isNotBlank(node.name) ? node.name : "发起人");
        userTask.setAssignee("${startUserId}");
        // 设置skipExpression，流程启动时设置_SKIP_INITIATOR=true即可自动通过
        userTask.setSkipExpression("${_SKIP_INITIATOR == true}");

        process.addFlowElement(userTask);

        // 处理后续节点
        ConvertResult nextResult = convertNode(node.childNode, process, prefix);

        if (nextResult != null) {
            connectFlow(process, taskId, nextResult.firstElement.getId(), null);
            return new ConvertResult(userTask, nextResult.exitElements);
        } else {
            return new ConvertResult(userTask, List.of(userTask));
        }
    }

    /**
     * 转换审批节点（type=1）
     * <p>
     * 生成UserTask，配置：
     * <ul>
     *   <li>assignee引用多实例循环变量${assignee}</li>
     *   <li>根据examineMode生成多实例配置</li>
     *   <li>存储三类extensionElement扩展：approveConfig、fieldPermission、buttonSetting</li>
     * </ul>
     */
    private static ConvertResult convertApprovalNode(Node node, Process process, String prefix) {
        String taskId = generateId(prefix, "approval", node.name);

        UserTask userTask = new UserTask();
        userTask.setId(taskId);
        userTask.setName(node.name);

        // 设置assignee引用多实例循环变量（由multiInstance的elementVariable定义）
        userTask.setAssignee("${assignee}");

        // 设置三类extensionElement扩展
        setApproverExtensionProperties(userTask, node);

        // 根据examineMode设置多实例配置
        setMultiInstanceConfig(userTask, node);

        process.addFlowElement(userTask);

        // 处理后续节点
        ConvertResult nextResult = convertNode(node.childNode, process, prefix);

        if (nextResult != null) {
            connectFlow(process, taskId, nextResult.firstElement.getId(), null);
            return new ConvertResult(userTask, nextResult.exitElements);
        } else {
            return new ConvertResult(userTask, List.of(userTask));
        }
    }

    /**
     * 转换抄送节点（type=2）
     * <p>
     * 生成ServiceTask节点，使用ccTaskDelegate委托表达式处理抄送逻辑
     * 仅存储approveConfig扩展，无表单、按钮扩展
     */
    private static ConvertResult convertCcNode(Node node, Process process, String prefix) {
        String taskId = generateId(prefix, "cc", node.name);

        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(taskId);
        serviceTask.setName(node.name);
        serviceTask.setImplementation("${ccTaskDelegate}");
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);

        // 仅存储approveConfig扩展（抄送无表单、按钮扩展）
        setExtensionElement(serviceTask, "approveConfig", buildApproveConfig(node).toString());

        process.addFlowElement(serviceTask);

        // 处理后续节点
        ConvertResult nextResult = convertNode(node.childNode, process, prefix);

        if (nextResult != null) {
            connectFlow(process, taskId, nextResult.firstElement.getId(), null);
            return new ConvertResult(serviceTask, nextResult.exitElements);
        } else {
            return new ConvertResult(serviceTask, List.of(serviceTask));
        }
    }

    /**
     * 转换条件节点（type=3）
     * <p>
     * 条件节点本身不生成FlowElement，只处理其子节点
     */
    private static ConvertResult convertConditionNode(Node node, Process process, String prefix) {
        // 条件节点本身不生成FlowElement，只处理其子节点
        if (node.childNode != null) {
            return convertNode(node.childNode, process, prefix);
        }
        return null;
    }

    /**
     * 转换路由节点（type=4）
     * <p>
     * 钉钉设计器路由节点结构：
     * <ul>
     *   <li>conditionNodes: 条件分支数组，每个分支含 conditionList（条件）和 childNode（分支内容）</li>
     *   <li>node.childNode: 分支汇合后的共同后续节点（可选）</li>
     * </ul>
     * <p>
     * 生成结构：入口排他网关 → [条件分支...] → 共同后续节点
     * <p>
     * 排他网关特性：只有一个分支会执行，因此不需要合并网关。
     * 所有分支出口直接连接到共同后续节点（或各自结束）。
     * <p>
     * 关键规则：
     * <ul>
     *   <li>isOther=true 的分支为兜底分支，设为网关 default 流（无条件表达式）</li>
     *   <li>空分支（childNode=null）直接连接到共同后续节点</li>
     *   <li>无共同后续节点时，分支出口作为路由出口</li>
     * </ul>
     */
    private static ConvertResult convertRouterNode(Node node, Process process, String prefix) {
        // 入口排他网关
        String gatewayInId = generateId(prefix, "gateway", node.name);
        ExclusiveGateway gatewayIn = new ExclusiveGateway();
        gatewayIn.setId(gatewayInId);
        gatewayIn.setName(node.name);
        process.addFlowElement(gatewayIn);

        // 先处理共同后续节点（node.childNode）
        ConvertResult commonNextResult = null;
        if (node.childNode != null) {
            commonNextResult = convertNode(node.childNode, process, prefix + "_next");
        }

        // 共同后续首节点ID（分支出口目标），无则null
        String commonNextId = (commonNextResult != null) ? commonNextResult.firstElement.getId() : null;

        // 收集所有分支的出口（用于返回）
        List<FlowElement> allExits = new ArrayList<>();

        // 处理条件分支
        if (node.conditionNodes != null && !node.conditionNodes.isEmpty()) {
            for (int i = 0; i < node.conditionNodes.size(); i++) {
                Node condNode = node.conditionNodes.get(i);
                String condPrefix = prefix + "_c" + i;

                // isOther=true 为兜底分支，无条件表达式；其他分支带条件
                String conditionExpr = condNode.isOther ? null : buildConditionExpression(condNode);

                if (condNode.childNode != null) {
                    // 有子节点的分支：入口网关 → 分支首节点
                    ConvertResult condResult = convertNode(condNode.childNode, process, condPrefix);
                    if (condResult != null) {
                        SequenceFlow flow = connectFlow(process, gatewayInId,
                                condResult.firstElement.getId(), conditionExpr);
                        if (condNode.isOther) {
                            gatewayIn.setDefaultFlow(Objects.requireNonNull(flow).getId());
                        }

                        // 分支出口 → 共同后续节点（如果有）
                        FlowElement branchExit = findLastElement(condResult);
                        if (commonNextId != null && !branchExit.getId().equals(commonNextId)) {
                            connectFlow(process, branchExit.getId(), commonNextId, null);
                        } else if (commonNextId == null) {
                            allExits.add(branchExit);
                        }
                    }
                } else if (commonNextId != null) {
                    // 空分支：入口网关 → 共同后续节点
                    SequenceFlow flow = connectFlow(process, gatewayInId, commonNextId, conditionExpr);
                    if (condNode.isOther) {
                        gatewayIn.setDefaultFlow(Objects.requireNonNull(flow).getId());
                    }
                }
            }
        }

        // 返回结果
        if (commonNextResult != null) {
            // 有共同后续：出口为共同后续的出口
            return new ConvertResult(gatewayIn, commonNextResult.exitElements);
        } else {
            // 无共同后续：分支出口作为路由出口
            return new ConvertResult(gatewayIn, allExits.isEmpty() ? List.of(gatewayIn) : allExits);
        }
    }

    // ==================== 审批人扩展属性设置 ====================

    /**
     * 设置审批节点的扩展属性
     * <p>
     * 采用拆分三类独立extensionElement扩展的方案：
     * <ul>
     *   <li>extensionElement name="approveConfig"：存放审批人规则JSON对象</li>
     *   <li>extensionElement name="fieldPermission"：存放表单字段权限JSON数组</li>
     *   <li>extensionElement name="buttonSetting"：存放节点操作按钮权限JSON数组</li>
     * </ul>
     * <p>
     * 重要：禁止使用flowable:xxx自定义属性，所有配置统一放入extensionElement扩展CDATA
     */
    private static void setApproverExtensionProperties(UserTask userTask, Node node) {
        // 构建approveConfig JSON对象
        JSONObject approveConfig = buildApproveConfig(node);

        // 设置扩展元素：approveConfig
        setExtensionElement(userTask, "approveConfig", approveConfig.toString());

        // 设置扩展元素：fieldPermission（从节点配置中读取）
        if (node.fieldPermission != null && !node.fieldPermission.isEmpty()) {
            setExtensionElement(userTask, "fieldPermission", node.fieldPermission.toString());
        }

        // 设置扩展元素：buttonSetting（从节点配置中读取）
        if (node.buttonSetting != null && !node.buttonSetting.isEmpty()) {
            setExtensionElement(userTask, "buttonSetting", node.buttonSetting.toString());
        }
    }

    /**
     * 设置多实例配置
     * <p>
     * 根据examineMode自动生成multiInstanceLoopCharacteristics标签：
     * <ul>
     *   <li>examineMode=1 顺序依次审批：sequential="true"</li>
     *   <li>examineMode=2 会签：sequential=false，完成条件${nrOfCompletedInstances * 100 / nrOfInstances >= signPct}</li>
     *   <li>examineMode=3 或签：sequential=false，完成条件${nrOfCompletedInstances >= 1}</li>
     * </ul>
     * <p>
     * 多实例collection和elementVariable通过flowable扩展属性设置，
     * 由BusinessAssigneeLoader在运行时设置assigneeList流程变量
     */
    private static void setMultiInstanceConfig(UserTask userTask, Node node) {
        MultiInstanceLoopCharacteristics multiInstance = new MultiInstanceLoopCharacteristics();

        // 通过flowable扩展属性设置collection和elementVariable
        ExtensionAttribute collectionAttr = new ExtensionAttribute();
        collectionAttr.setNamespace(FLOWABLE_NS);
        collectionAttr.setNamespacePrefix("flowable");
        collectionAttr.setName("collection");
        collectionAttr.setValue("assigneeList");
        multiInstance.addAttribute(collectionAttr);

        ExtensionAttribute elementVariableAttr = new ExtensionAttribute();
        elementVariableAttr.setNamespace(FLOWABLE_NS);
        elementVariableAttr.setNamespacePrefix("flowable");
        elementVariableAttr.setName("elementVariable");
        elementVariableAttr.setValue("assignee");
        multiInstance.addAttribute(elementVariableAttr);

        switch (node.examineMode) {
            case 1: // 顺序依次审批
                multiInstance.setSequential(true);
                break;
            case 2: // 会签
                multiInstance.setSequential(false);
                multiInstance.setCompletionCondition(
                    "${nrOfCompletedInstances * 100 / nrOfInstances >= " + node.signPct + "}");
                break;
            case 3: // 或签
                multiInstance.setSequential(false);
                multiInstance.setCompletionCondition("${nrOfCompletedInstances >= 1}");
                break;
            default:
                multiInstance.setSequential(false);
                multiInstance.setCompletionCondition("${nrOfCompletedInstances >= 1}");
                break;
        }

        userTask.setLoopCharacteristics(multiInstance);
    }

    /**
     * 构建审批人配置JSON对象
     */
    private static JSONObject buildApproveConfig(Node node) {
        JSONObject config = new JSONObject();

        // 基本配置
        config.set("settype", node.settype);
        config.set("examineMode", node.examineMode);
        config.set("signPct", node.signPct);
        config.set("noHanderAction", node.noHanderAction);
        config.set("ccSelfSelectFlag", node.ccSelfSelectFlag);

        // 审批人列表配置
        if (node.nodeUserList != null && !node.nodeUserList.isEmpty()) {
            config.set("nodeUserList", node.nodeUserList);
        }
        if (node.nodeRoleList != null && !node.nodeRoleList.isEmpty()) {
            config.set("nodeRoleList", node.nodeRoleList);
        }
        if (node.nodePostList != null && !node.nodePostList.isEmpty()) {
            config.set("nodePostList", node.nodePostList);
        }
        if (node.nodeDeptHeadList != null && !node.nodeDeptHeadList.isEmpty()) {
            config.set("nodeDeptHeadList", node.nodeDeptHeadList);
        }
        if (node.candidateParam != null && !node.candidateParam.isEmpty()) {
            config.set("candidateParam", node.candidateParam);
        }

        // 表单字段ID（表单内人员类型时使用）
        if (node.candidateParam != null && !node.candidateParam.isEmpty()) {
            config.set("formFieldId", node.candidateParam.get(0));
        }

        return config;
    }

    /**
     * 设置扩展元素（flowable:xxx子元素）
     * <p>
     * 使用extensionElements子元素存储JSON配置，CDATA包裹避免XML转义
     * 生成的XML结构：
     * <pre>
     * &lt;extensionElements&gt;
     *   &lt;flowable:approveConfig&gt;&lt;![CDATA[{"settype":1,...}]]&gt;&lt;/flowable:approveConfig&gt;
     * &lt;/extensionElements&gt;
     * </pre>
     */
    private static void setExtensionElement(FlowElement element, String name, String value) {
        if (StrUtil.isNotBlank(value)) {
            ExtensionElement extElement = new ExtensionElement();
            extElement.setNamespace(FLOWABLE_NS);
            extElement.setNamespacePrefix("flowable");
            extElement.setName(name);
            extElement.setElementText(value);
            element.addExtensionElement(extElement);
        }
    }

    // ==================== 条件表达式 ====================

    /**
     * 构建条件表达式
     * <p>
     * 将前端条件列表转换为Flowable UEL表达式，多个条件之间用 AND 连接
     *
     * @param condNode 条件节点
     * @return UEL条件表达式，如: ${days > 3 && type == '1'}
     */
    private static String buildConditionExpression(Node condNode) {
        if (condNode.conditionList == null || condNode.conditionList.isEmpty()) {
            return null;
        }

        List<String> subExprs = new ArrayList<>();

        for (JSONObject cond : condNode.conditionList) {
            String columnId = cond.getStr("columnId");
            String optType = cond.getStr("optType");
            String zdy1 = cond.getStr("zdy1");
            String zdy2 = cond.getStr("zdy2");
            String opt1 = cond.getStr("opt1");
            String opt2 = cond.getStr("opt2");
            String columnType = cond.getStr("columnType");

            if (StrUtil.isBlank(columnId)) {
                continue;
            }

            // 如果optType为空，根据其他字段推断
            if (StrUtil.isBlank(optType)) {
                if (StrUtil.isNotBlank(zdy1) && StrUtil.isNotBlank(zdy2)) {
                    optType = "1"; // 介于两个值之间
                } else if (StrUtil.isNotBlank(opt1)) {
                    optType = "2"; // 比较操作
                } else if (StrUtil.isNotBlank(zdy1)) {
                    // 只有zdy1，可能是包含或不包含，默认为包含
                    optType = "3";
                } else {
                    continue;
                }
            }

            String expr = buildSingleCondition(columnId, optType, zdy1, zdy2, opt1, opt2, columnType);
            if (StrUtil.isNotBlank(expr)) {
                subExprs.add(expr);
            }
        }

        if (subExprs.isEmpty()) {
            return null;
        }

        // 多条件之间用 AND 连接
        if (subExprs.size() == 1) {
            return "${" + subExprs.get(0) + "}";
        }

        StringBuilder sb = new StringBuilder("${");
        for (int i = 0; i < subExprs.size(); i++) {
            if (i > 0) {
                sb.append(" && ");
            }
            sb.append(subExprs.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 构建单个条件表达式
     * <p>
     * optType含义：
     * - "1": 介于两个值之间 (zdy1 ~ zdy2)
     * - "2": 比较操作 (zdy1 opt1 value)
     * - "3": 包含操作
     * - "4": 不包含操作
     */
    private static String buildSingleCondition(String columnId, String optType, String zdy1, String zdy2,
                                         String opt1, String opt2, String columnType) {
        switch (optType) {
            case "1":
                // 介于: columnId >= zdy1 && columnId <= zdy2
                if (StrUtil.isBlank(zdy1) || StrUtil.isBlank(zdy2)) {
                    return null;
                }
                return columnId + " >= " + formatValue(zdy1, columnType)
                        + " && " + columnId + " <= " + formatValue(zdy2, columnType);

            case "2":
                // 比较: columnId opt1 zdy1
                if (StrUtil.isBlank(zdy1) || StrUtil.isBlank(opt1)) {
                    return null;
                }
                String operator = mapOperator(opt1);
                return columnId + " " + operator + " " + formatValue(zdy1, columnType);

            case "3":
                // 包含
                if (StrUtil.isBlank(zdy1)) {
                    return null;
                }
                return columnId + " == " + formatValue(zdy1, columnType);

            case "4":
                // 不包含
                if (StrUtil.isBlank(zdy1)) {
                    return null;
                }
                return columnId + " != " + formatValue(zdy1, columnType);

            default:
                log.warn("未知的条件操作类型: {}", optType);
                return null;
        }
    }

    /**
     * 映射前端操作符到Java操作符
     */
    private static String mapOperator(String opt) {
        if (opt == null) {
            return "==";
        }
        switch (opt) {
            case ">":
            case "<":
            case ">=":
            case "<=":
            case "==":
            case "!=":
                return opt;
            case "null":
                return "== null";
            case "not_null":
                return "!= null";
            default:
                return opt;
        }
    }

    /**
     * 格式化条件值（字符串类型加引号）
     */
    private static String formatValue(String value, String columnType) {
        if (StrUtil.isBlank(value)) {
            return "null";
        }
        if ("String".equalsIgnoreCase(columnType)) {
            return "'" + value + "'";
        }
        return value;
    }

    // ==================== 工具方法 ====================

    /**
     * 创建序列流并添加到流程
     * <p>
     * 防护规则：
     * <ul>
     *   <li>禁止自循环（sourceRef == targetRef）</li>
     *   <li>禁止重复连接（相同sourceRef+targetRef）</li>
     *   <li>同时注册到source和target元素的outgoing/incoming列表</li>
     * </ul>
     */
    private static SequenceFlow connectFlow(Process process, String sourceRef, String targetRef, String conditionExpr) {
        // 禁止自循环
        if (sourceRef == null || targetRef == null || sourceRef.equals(targetRef)) {
            log.warn("跳过无效的sequenceFlow: sourceRef={}, targetRef={}", sourceRef, targetRef);
            return null;
        }

        // 检查是否已存在相同的连接
        String flowId = sourceRef + "_to_" + targetRef;
        if (process.getFlowElement(flowId) != null) {
            log.debug("sequenceFlow已存在，跳过: {}", flowId);
            return (SequenceFlow) process.getFlowElement(flowId);
        }

        SequenceFlow flow = new SequenceFlow();
        flow.setId(flowId);
        flow.setSourceRef(sourceRef);
        flow.setTargetRef(targetRef);
        if (StrUtil.isNotBlank(conditionExpr)) {
            flow.setConditionExpression(conditionExpr);
        }
        process.addFlowElement(flow);

        // 关键：同时注册到source和target元素，否则getOutgoingFlows()返回空
        FlowElement sourceElement = process.getFlowElement(sourceRef);
        FlowElement targetElement = process.getFlowElement(targetRef);
        if (sourceElement instanceof FlowNode sourceNode) {
            sourceNode.getOutgoingFlows().add(flow);
        }
        if (targetElement instanceof FlowNode targetNode) {
            targetNode.getIncomingFlows().add(flow);
        }

        return flow;
    }

    /** ID计数器，避免哈希冲突 */
    private static int idCounter = 0;

    /**
     * 生成唯一ID
     * <p>
     * 使用递增计数器确保唯一性，避免哈希冲突导致的重复ID
     */
    private static synchronized String generateId(String prefix, String type, String name) {
        return prefix + "_" + type + "_" + (++idCounter);
    }

    /**
     * 查找转换结果链中的最后一个元素（最远出口）
     */
    private static FlowElement findLastElement(ConvertResult result) {
        if (result == null || result.exitElements == null || result.exitElements.isEmpty()) {
            return result != null ? result.firstElement : null;
        }
        return result.exitElements.get(0);
    }

    /**
     * 解析JSON节点为内部Node对象
     */
    private static Node parseNode(JSONObject json) {
        if (json == null) {
            return null;
        }

        Node node = new Node();
        node.name = json.getStr("nodeName", "未命名");
        node.type = json.getInt("type", -1);
        node.settype = json.getInt("settype", 0);
        node.examineMode = json.getInt("examineMode", 0);
        node.signPct = json.getInt("signPct", 100);
        node.noHanderAction = json.getInt("noHanderAction", 0);
        node.ccSelfSelectFlag = json.getInt("ccSelfSelectFlag", 0);
        node.isOther = json.getBool("isOther", false);
        node.priorityLevel = json.getInt("priorityLevel", 0);

        // 解析列表字段
        node.nodeUserList = parseJsonArray(json.getJSONArray("nodeUserList"));
        node.nodeRoleList = parseJsonArray(json.getJSONArray("nodeRoleList"));
        node.nodePostList = parseJsonArray(json.getJSONArray("nodePostList"));
        node.nodeDeptHeadList = parseJsonArray(json.getJSONArray("nodeDeptHeadList"));
        node.conditionList = parseJsonArray(json.getJSONArray("conditionList"));
        node.candidateParam = parseStringList(json.getJSONArray("candidateParam"));

        // 解析表单字段权限和按钮配置（兼容两种字段名）
        node.fieldPermission = json.getJSONArray("fieldPermission");
        if (node.fieldPermission == null) {
            node.fieldPermission = json.getJSONArray("fieldsPermission");
        }
        if (node.fieldPermission == null) {
            node.fieldPermission = new JSONArray();
        }
        node.buttonSetting = json.getJSONArray("buttonSetting");
        if (node.buttonSetting == null) {
            node.buttonSetting = json.getJSONArray("buttonsSetting");
        }
        if (node.buttonSetting == null) {
            node.buttonSetting = new JSONArray();
        }

        // 解析子节点
        JSONObject childNodeJson = json.getJSONObject("childNode");
        if (childNodeJson != null) {
            node.childNode = parseNode(childNodeJson);
        }

        // 解析条件分支节点
        JSONArray conditionNodesJson = json.getJSONArray("conditionNodes");
        if (conditionNodesJson != null && !conditionNodesJson.isEmpty()) {
            node.conditionNodes = new ArrayList<>();
            for (int i = 0; i < conditionNodesJson.size(); i++) {
                JSONObject condJson = conditionNodesJson.getJSONObject(i);
                node.conditionNodes.add(parseNode(condJson));
            }
        }

        return node;
    }

    /**
     * 解析JSONArray为JSONObject列表
     */
    private static List<JSONObject> parseJsonArray(JSONArray array) {
        if (array == null || array.isEmpty()) {
            return new ArrayList<>();
        }
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            list.add(array.getJSONObject(i));
        }
        return list;
    }

    /**
     * 解析JSONArray为String列表
     */
    private static List<String> parseStringList(JSONArray array) {
        if (array == null || array.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            list.add(array.getStr(i));
        }
        return list;
    }

    // ==================== 反向转换方法 ====================

    /**
     * 递归转换FlowElement为JSON节点
     */
    private static JSONObject convertFlowElementToJson(FlowElement element, Process process) {
        if (element == null) {
            return null;
        }

        JSONObject node = new JSONObject();

        if (element instanceof StartEvent) {
            // 开始事件 → 发起人节点
            node.set("nodeName", element.getName() != null ? element.getName() : "发起人");
            node.set("type", 0);

            // 查找下一个节点
            FlowElement nextElement = findNextElement(element, process);
            if (nextElement != null) {
                node.set("childNode", convertFlowElementToJson(nextElement, process));
            }

        } else if (element instanceof UserTask userTask) {
            node.set("nodeName", userTask.getName());

            // 读取approveConfig扩展
            JSONObject approveConfig = getApproveConfigFromElement(userTask);
            if (!approveConfig.isEmpty()) {
                int setType = approveConfig.getInt("settype", 0);
                node.set("type", 1); // 审批节点
                node.set("settype", setType);
                node.set("examineMode", approveConfig.getInt("examineMode", 0));
                node.set("signPct", approveConfig.getInt("signPct", 100));
                node.set("noHanderAction", approveConfig.getInt("noHanderAction", 0));
                node.set("ccSelfSelectFlag", approveConfig.getInt("ccSelfSelectFlag", 0));

                // 审批人列表
                if (approveConfig.containsKey("nodeUserList")) {
                    node.set("nodeUserList", approveConfig.getJSONArray("nodeUserList"));
                }
                if (approveConfig.containsKey("nodeRoleList")) {
                    node.set("nodeRoleList", approveConfig.getJSONArray("nodeRoleList"));
                }
                if (approveConfig.containsKey("nodePostList")) {
                    node.set("nodePostList", approveConfig.getJSONArray("nodePostList"));
                }
                if (approveConfig.containsKey("nodeDeptHeadList")) {
                    node.set("nodeDeptHeadList", approveConfig.getJSONArray("nodeDeptHeadList"));
                }
                if (approveConfig.containsKey("candidateParam")) {
                    node.set("candidateParam", approveConfig.getJSONArray("candidateParam"));
                }
            } else {
                node.set("type", 1);
                node.set("settype", 0);
            }

            // 读取fieldPermission扩展
            JSONArray fieldPermission = getFieldPermissionFromElement(userTask);
            if (!fieldPermission.isEmpty()) {
                node.set("fieldPermission", fieldPermission);
            }

            // 读取buttonSetting扩展
            JSONArray buttonSetting = getButtonSettingFromElement(userTask);
            if (!buttonSetting.isEmpty()) {
                node.set("buttonSetting", buttonSetting);
            }

            // 查找下一个节点
            FlowElement nextElement = findNextElement(element, process);
            if (nextElement != null) {
                node.set("childNode", convertFlowElementToJson(nextElement, process));
            }

        } else if (element instanceof ServiceTask serviceTask) {
            // ServiceTask → 抄送节点
            node.set("nodeName", serviceTask.getName());
            node.set("type", 2); // 抄送节点

            // 读取approveConfig扩展
            JSONObject approveConfig = getApproveConfigFromElement(serviceTask);
            if (!approveConfig.isEmpty()) {
                int setType = approveConfig.getInt("settype", 0);
                node.set("settype", setType);
                node.set("ccSelfSelectFlag", approveConfig.getInt("ccSelfSelectFlag", 0));

                // 审批人列表
                if (approveConfig.containsKey("nodeUserList")) {
                    node.set("nodeUserList", approveConfig.getJSONArray("nodeUserList"));
                }
                if (approveConfig.containsKey("nodeRoleList")) {
                    node.set("nodeRoleList", approveConfig.getJSONArray("nodeRoleList"));
                }
                if (approveConfig.containsKey("nodePostList")) {
                    node.set("nodePostList", approveConfig.getJSONArray("nodePostList"));
                }
                if (approveConfig.containsKey("nodeDeptHeadList")) {
                    node.set("nodeDeptHeadList", approveConfig.getJSONArray("nodeDeptHeadList"));
                }
                if (approveConfig.containsKey("candidateParam")) {
                    node.set("candidateParam", approveConfig.getJSONArray("candidateParam"));
                }
            }

            // 查找下一个节点
            FlowElement nextElement = findNextElement(element, process);
            if (nextElement != null) {
                node.set("childNode", convertFlowElementToJson(nextElement, process));
            }

        } else if (element instanceof ExclusiveGateway gateway) {
            node.set("nodeName", gateway.getName());
            node.set("type", 4); // 路由节点

            // 处理条件分支
            List<SequenceFlow> outgoingFlows = gateway.getOutgoingFlows();
            if (outgoingFlows != null && !outgoingFlows.isEmpty()) {
                JSONArray conditionNodes = new JSONArray();

                for (SequenceFlow flow : outgoingFlows) {
                    if (flow.getConditionExpression() != null) {
                        // 条件分支
                        JSONObject condNode = new JSONObject();
                        condNode.set("nodeName", flow.getName() != null ? flow.getName() : "条件分支");
                        condNode.set("type", 3);
                        condNode.set("conditionExpression", flow.getConditionExpression());

                        // 查找条件分支的目标节点
                        FlowElement targetElement = process.getFlowElement(flow.getTargetRef());
                        if (targetElement != null) {
                            condNode.set("childNode", convertFlowElementToJson(targetElement, process));
                        }

                        conditionNodes.add(condNode);
                    }
                }

                if (!conditionNodes.isEmpty()) {
                    node.set("conditionNodes", conditionNodes);
                }
            }

        } else if (element instanceof EndEvent) {
            // 结束事件，不生成节点
            return null;
        }

        return node;
    }

    /**
     * 查找下一个FlowElement
     */
    private static FlowElement findNextElement(FlowElement currentElement, Process process) {
        List<SequenceFlow> outgoingFlows = null;

        if (currentElement instanceof StartEvent startEvt) {
            outgoingFlows = startEvt.getOutgoingFlows();
        } else if (currentElement instanceof UserTask ut) {
            outgoingFlows = ut.getOutgoingFlows();
        } else if (currentElement instanceof ServiceTask st) {
            outgoingFlows = st.getOutgoingFlows();
        } else if (currentElement instanceof ExclusiveGateway gw) {
            outgoingFlows = gw.getOutgoingFlows();
        }

        if (outgoingFlows != null && !outgoingFlows.isEmpty()) {
            SequenceFlow nextFlow = outgoingFlows.get(0);
            return process.getFlowElement(nextFlow.getTargetRef());
        }

        return null;
    }

    /**
     * 从UserTask或ServiceTask获取approveConfig
     */
    private static JSONObject getApproveConfigFromElement(FlowElement element) {
        try {
            String configJson = getExtensionElementValue(element, "approveConfig");
            if (StrUtil.isNotBlank(configJson)) {
                return JSONUtil.parseObj(configJson);
            }
            return new JSONObject();
        } catch (Exception e) {
            log.error("解析approveConfig失败：elementId={}", element.getId(), e);
            return new JSONObject();
        }
    }

    /**
     * 从UserTask获取fieldPermission
     */
    private static JSONArray getFieldPermissionFromElement(UserTask userTask) {
        try {
            String permissionJson = getExtensionElementValue(userTask, "fieldPermission");
            if (StrUtil.isNotBlank(permissionJson)) {
                return JSONUtil.parseArray(permissionJson);
            }
            return new JSONArray();
        } catch (Exception e) {
            log.error("解析fieldPermission失败：taskId={}", userTask.getId(), e);
            return new JSONArray();
        }
    }

    /**
     * 从UserTask获取buttonSetting
     */
    private static JSONArray getButtonSettingFromElement(UserTask userTask) {
        try {
            String settingJson = getExtensionElementValue(userTask, "buttonSetting");
            if (StrUtil.isNotBlank(settingJson)) {
                return JSONUtil.parseArray(settingJson);
            }
            return new JSONArray();
        } catch (Exception e) {
            log.error("解析buttonSetting失败：taskId={}", userTask.getId(), e);
            return new JSONArray();
        }
    }

    /**
     * 获取扩展元素的值
     */
    private static String getExtensionElementValue(FlowElement element, String name) {
        List<ExtensionElement> extensions = element.getExtensionElements().get(name);
        if (extensions != null && !extensions.isEmpty()) {
            return extensions.get(0).getElementText();
        }
        return null;
    }

    // ==================== 内部类 ====================

    /**
     * 内部节点模型
     */
    @Data
    private static class Node {
        String name;
        int type;
        int settype;
        int examineMode;
        int signPct;
        int noHanderAction;
        int ccSelfSelectFlag;
        boolean isOther;
        int priorityLevel;

        List<JSONObject> nodeUserList = new ArrayList<>();
        List<JSONObject> nodeRoleList = new ArrayList<>();
        List<JSONObject> nodePostList = new ArrayList<>();
        List<JSONObject> nodeDeptHeadList = new ArrayList<>();
        List<JSONObject> conditionList = new ArrayList<>();
        List<String> candidateParam = new ArrayList<>();

        JSONArray fieldPermission = new JSONArray();
        JSONArray buttonSetting = new JSONArray();

        Node childNode;
        List<Node> conditionNodes;
    }

    /**
     * 转换结果：首个元素 + 末尾出口元素列表
     */
    private static class ConvertResult {
        final FlowElement firstElement;
        final List<FlowElement> exitElements;

        ConvertResult(FlowElement firstElement, List<FlowElement> exitElements) {
            this.firstElement = firstElement;
            this.exitElements = exitElements != null ? exitElements : List.of();
        }
    }
}