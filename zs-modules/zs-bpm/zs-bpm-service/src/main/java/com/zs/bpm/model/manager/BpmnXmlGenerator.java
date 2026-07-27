package com.zs.bpm.model.manager;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zs.bpm.model.domain.dto.*;
import com.zs.common.core.enums.bpmn.FlowableActivityTypeEnum;
import jakarta.annotation.Resource;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BpmnXmlGenerator {

    private static final String FLOWABLE_NS = "http://flowable.org/bpmn";
    private static final String FLOWABLE_PREFIX = "flowable";

    @Resource
    private ConditionConverter conditionConverter;


    /**
     * 入口：扁平化结果 → 标准BpmnModel（与当前XML完全一致）
     */
    public BpmnModel generate(FlatFlowResult flatResult, String processKey, String processName) {
        BpmnModel bpmnModel = initBpmnModel();
        Process process = new Process();
        process.setId(processKey);
        process.setName(flatResult.getProcessName());
        process.setExecutable(true);
        bpmnModel.addProcess(process);

        Map<String, FlowElement> elementMap = new HashMap<>();
        // 1. 构建所有节点
        for (FlowNodeDTO node : flatResult.getNodes()) {
            FlowElement element = buildFlowElement(node);
            elementMap.put(node.getId(), element);
            process.addFlowElement(element);
        }

        // 2. 构建所有连线（双条件存储）
        for (FlowEdgeDTO edge : flatResult.getEdges()) {
            SequenceFlow flow = buildSequenceFlow(edge);
            process.addFlowElement(flow);
        }

        // 3. 设置网关默认分支
        flatResult.getNodes().stream()
                .filter(n -> FlowableActivityTypeEnum.EXCLUSIVE_GATEWAY.getValue().equals(n.getType()) && n.getDefaultFlowId() != null)
                .forEach(n -> {
                    ExclusiveGateway gateway = (ExclusiveGateway) elementMap.get(n.getId());
                    gateway.setDefaultFlow(n.getDefaultFlowId());
                });

        // 4. 官方自动布局（生成与XML一致的横向DI图形）
        BpmnAutoLayout autoLayout = new BpmnAutoLayout(bpmnModel);
        autoLayout.setTaskWidth(160);
        autoLayout.setTaskHeight(60);
        autoLayout.setGatewaySize(40);
        autoLayout.setEventSize(30);
        autoLayout.execute();

        return bpmnModel;
    }

    private BpmnModel initBpmnModel() {
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addNamespace(FLOWABLE_PREFIX, FLOWABLE_NS);
        return bpmnModel;
    }

    private FlowElement buildFlowElement(FlowNodeDTO node) {
        // 注：switch case 需编译期常量，此处直接引用 FlowableActivityTypeEnum 的 value 字符串
        return switch (node.getType()) {
            case "startEvent" -> buildStartEvent(node);
            case "endEvent" -> buildEndEvent(node);
            case "userTask" -> buildUserTask(node);
            case "serviceTask" -> buildServiceTask(node);
            case "exclusiveGateway" -> buildExclusiveGateway(node);
            default -> throw new IllegalArgumentException("不支持的节点类型: " + node.getType());
        };
    }

    private StartEvent buildStartEvent(FlowNodeDTO node) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(node.getId());
        startEvent.setName(node.getName());
        return startEvent;
    }

    private EndEvent buildEndEvent(FlowNodeDTO node) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(node.getId());
        endEvent.setName(node.getName());
        return endEvent;
    }

    private ExclusiveGateway buildExclusiveGateway(FlowNodeDTO node) {
        ExclusiveGateway gateway = new ExclusiveGateway();
        gateway.setId(node.getId());
        gateway.setName(node.getName());
        return gateway;
    }

    /**
     * 构建UserTask（发起人/审批人）
     * <p>
     * 发起人节点（config.type=0）：简化配置，仅设置负责人，
     * 不含 extensionElements 和 multiInstanceLoopCharacteristics，
     * 流程启动后由代码手动添加审批意见并自动完成。
     * <p>
     * 审批节点（config.type=1）：完整扩展字段+多实例配置，
     * assignee 绑定多实例元素变量 {@code ${approver}}，
     * 标签顺序严格与XML一致：基础参数→兜底→权限(双写)→按钮(双写)→监听器
     */
    private UserTask buildUserTask(FlowNodeDTO node) {
        UserTask userTask = new UserTask();
        userTask.setId(node.getId());
        userTask.setName(node.getName());
        NodeConfigDTO config = node.getRawConfig();

        // 发起人节点：仅设置负责人，流程启动后由代码手动完成（添加审批意见后自动通过）
        if (config != null && config.getType() != null && config.getType() == 0) {
            userTask.setAssignee("${startUserId}");
            return userTask;
        }

        // 审批节点：绑定多实例元素变量，引擎创建任务时写入 ASSIGNEE_
        userTask.setAssignee("${approver}");

        // 审批节点：nodeConfig JSON + 表单权限/按钮 + 监听器
        List<ExtensionElement> extensions = buildUserTaskExtensions(config);
        userTask.setExtensionElements(wrapExtMap(extensions));

        // 多实例配置（统一变量名 assigneeList，三种审批模式）
        MultiInstanceLoopCharacteristics multi = buildMultiInstance(config);
        userTask.setLoopCharacteristics(multi);

        return userTask;
    }

    /**
     * 构建UserTask扩展字段（标准化顺序）
     * <p>
     * nodeConfig JSON（统一审批参数）→ 表单权限双写 → 按钮配置双写 → 全生命周期监听器
     */
    private List<ExtensionElement> buildUserTaskExtensions(NodeConfigDTO config) {
        List<ExtensionElement> extensions = new ArrayList<>();

        // ========== 1. 审批参数：分散字段 → 单个 nodeConfig JSON ==========
        extensions.add(buildNodeConfigExt(config, config.getType() == 0 ? "0" : "11"));

        // ========== 2. 表单权限（JSON+平铺双写）==========
        if (config.getFieldsPermission() != null && !config.getFieldsPermission().isEmpty()) {
            // JSON回显字段
            extensions.add(buildTextExt("fieldsPermissionJson", JSONUtil.toJsonStr(config.getFieldsPermission())));
            // 平铺降级字段
            for (FieldPermissionDTO field : config.getFieldsPermission()) {
                ExtensionElement ext = new ExtensionElement();
                ext.setName("fieldsPermission");
                ext.setNamespace(FLOWABLE_NS);
                ext.setNamespacePrefix(FLOWABLE_PREFIX);
                ext.addAttribute(buildAttr("field", field.getField()));
                ext.addAttribute(buildAttr("title", field.getTitle()));
                ext.addAttribute(buildAttr("permission", field.getPermission()));
                extensions.add(ext);
            }
        }

        // ========== 3. 操作按钮（JSON+平铺双写）==========
        if (config.getButtonsSetting() != null && !config.getButtonsSetting().isEmpty()) {
            // JSON回显字段
            extensions.add(buildTextExt("buttonsSettingJson", JSONUtil.toJsonStr(config.getButtonsSetting())));
            // 平铺降级字段
            for (ButtonSettingDTO btn : config.getButtonsSetting()) {
                ExtensionElement ext = new ExtensionElement();
                ext.setName("buttonsSetting");
                ext.setNamespace(FLOWABLE_NS);
                ext.setNamespacePrefix(FLOWABLE_PREFIX);
                ext.addAttribute(buildAttr("id", String.valueOf(btn.getId())));
                ext.addAttribute(buildAttr("displayName", btn.getDisplayName()));
                ext.addAttribute(buildAttr("enable", String.valueOf(btn.getEnable())));
                extensions.add(ext);
            }
        }

        // ========== 4. 全生命周期监听器 ==========
        extensions.add(buildListenerExt("executionListener", "start", "${dynamicApproverCalcListener}"));
        // 审批节点追加end监听器，发起人节点不追加
        if (config.getType() == 1) {
            extensions.add(buildListenerExt("executionListener", "end", "${nodeEndNotifyListener}"));
        }
        extensions.add(buildListenerExt("taskListener", "create", "${taskAssignListener}"));
        extensions.add(buildListenerExt("taskListener", "complete", "${taskCompleteListener}"));

        return extensions;
    }

    /**
     * 构建抄送 ServiceTask
     * <p>
     * 使用 nodeConfig JSON 统一存储抄送参数（settype/candidateParam/ccTiming），
     * 仅保留表单权限双写和抄送专属监听器。
     */
    private ServiceTask buildServiceTask(FlowNodeDTO node) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(node.getId());
        serviceTask.setName(node.getName());
        serviceTask.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
        serviceTask.setImplementation("${bpmCopyTaskDelegate}");

        NodeConfigDTO config = node.getRawConfig();
        List<ExtensionElement> extensions = new ArrayList<>();

        // 抄送参数：分散字段 → 单个 nodeConfig JSON
        extensions.add(buildCcConfigExt(config));

        // 表单权限双写
        if (config.getFieldsPermission() != null && !config.getFieldsPermission().isEmpty()) {
            extensions.add(buildTextExt("fieldsPermissionJson", JSONUtil.toJsonStr(config.getFieldsPermission())));
            for (FieldPermissionDTO field : config.getFieldsPermission()) {
                ExtensionElement ext = new ExtensionElement();
                ext.setName("fieldsPermission");
                ext.setNamespace(FLOWABLE_NS);
                ext.setNamespacePrefix(FLOWABLE_PREFIX);
                ext.addAttribute(buildAttr("field", field.getField()));
                ext.addAttribute(buildAttr("title", field.getTitle()));
                ext.addAttribute(buildAttr("permission", field.getPermission()));
                extensions.add(ext);
            }
        }

        // 抄送专属监听器
        extensions.add(buildListenerExt("executionListener", "start", "${ccNotificationListener}"));

        serviceTask.setExtensionElements(wrapExtMap(extensions));
        return serviceTask;
    }

    /**
     * 构建顺序流（双条件存储）
     */
    private SequenceFlow buildSequenceFlow(FlowEdgeDTO edge) {
        SequenceFlow flow = new SequenceFlow();
        flow.setId(edge.getId());
        flow.setSourceRef(edge.getSourceId());
        flow.setTargetRef(edge.getTargetId());

        if (edge.getConditionConfig() != null) {
            // 1. EL执行表达式
            String elExpr = conditionConverter.convertToEl(edge.getConditionConfig());
            flow.setConditionExpression(elExpr);

            // 2. conditionJson回显
            String json = conditionConverter.convertToJson(edge.getConditionConfig());
            ExtensionElement jsonExt = buildTextExt("conditionJson", json);
            flow.setExtensionElements(wrapExtMap(Collections.singletonList(jsonExt)));
        }
        return flow;
    }

    /**
     * 构建多实例配置
     * <p>
     * 所有审批节点统一使用 {@code assigneeList} 流程变量，
     * 消除变量名与节点 ID 的强耦合，监听器无需感知当前节点 ID。
     * <p>
     * 审批模式：1=依次审批(串行)、2=会签(并行+比例)、3=或签(并行+一票通过)
     */
    private MultiInstanceLoopCharacteristics buildMultiInstance(NodeConfigDTO config) {
        MultiInstanceLoopCharacteristics multi = new MultiInstanceLoopCharacteristics();
        // 统一变量名，不再区分节点 ID
        multi.setInputDataItem("${assigneeList}");
        multi.setElementVariable("approver");

        Integer mode = config.getExamineMode();
        // 发起人节点或未配置审批模式时，默认按依次审批处理
        if (mode == null) {
            mode = 3;
        }

        switch (mode) {
            case 1: // 依次审批：串行，全部完成
                multi.setSequential(true);
                multi.setCompletionCondition("${nrOfCompletedInstances >= nrOfInstances}");
                break;
            case 2: // 会签：并行，按比例通过（默认全票）
                multi.setSequential(false);
                int passRate = Objects.requireNonNullElse(config.getSignPct(), 100);
                multi.setCompletionCondition("${nrOfCompletedInstances >= nrOfInstances * " + passRate + " / 100}");
                break;
            case 3: // 或签：并行，一票通过
            default:
                multi.setSequential(false);
                multi.setCompletionCondition("${nrOfCompletedInstances > 0}");
                break;
        }
        return multi;
    }

    // ==================== 工具方法 ====================
    private ExtensionElement buildTextExt(String name, String text) {
        ExtensionElement element = new ExtensionElement();
        element.setName(name);
        element.setNamespace(FLOWABLE_NS);
        element.setNamespacePrefix(FLOWABLE_PREFIX);
        element.setElementText(text);
        return element;
    }

    private ExtensionElement buildListenerExt(String type, String event, String delegate) {
        ExtensionElement listener = new ExtensionElement();
        listener.setName(type);
        listener.setNamespace(FLOWABLE_NS);
        listener.setNamespacePrefix(FLOWABLE_PREFIX);
        listener.addAttribute(buildAttr("event", event));
        listener.addAttribute(buildAttr("delegateExpression", delegate));
        return listener;
    }

    private ExtensionAttribute buildAttr(String name, String value) {
        ExtensionAttribute attr = new ExtensionAttribute();
        attr.setName(name);
        attr.setValue(value);
        return attr;
    }

    /**
     * 构建审批节点 nodeConfig JSON 扩展元素
     * <p>
     * 将 settype/candidateParam/examineMode/signPct/noHanderAction/backupUsers
     * 等纯审批参数合并为单个 {@code <flowable:nodeConfig>} JSON 元素，
     * 替代原有的 8 个分散扩展元素，大幅减少 XML 冗余。
     *
     * @param config   节点配置
     * @param nodeType 节点类型标识（"0"=发起人 / "11"=审批人）
     */
    private ExtensionElement buildNodeConfigExt(NodeConfigDTO config, String nodeType) {
        JSONObject cfg = new JSONObject();
        cfg.set("settype", config.getSettype());
        if (config.getCandidateParam() != null && !config.getCandidateParam().isEmpty()) {
            cfg.set("candidateParam", String.join(",", config.getCandidateParam()));
        }
        if (config.getExamineMode() != null) {
            cfg.set("examineMode", config.getExamineMode());
        }
        if (config.getSignPct() != null) {
            cfg.set("signPct", config.getSignPct());
        }
        if (config.getNoHanderAction() != null) {
            cfg.set("noHanderAction", config.getNoHanderAction());
        }
        if (config.getBackupUsers() != null) {
            cfg.set("backupUsers", config.getBackupUsers());
        }
        cfg.set("nodeType", nodeType);
        cfg.set("approveType", "1");
        return buildTextExt("nodeConfig", cfg.toString());
    }

    /**
     * 构建抄送节点 nodeConfig JSON 扩展元素
     * <p>
     * 将抄送参数（settype/candidateParam/ccTiming）合并为单个 JSON。
     */
    private ExtensionElement buildCcConfigExt(NodeConfigDTO config) {
        JSONObject cfg = new JSONObject();
        if (config.getSettype() != null) {
            cfg.set("settype", config.getSettype());
        }
        if (config.getCandidateParam() != null && !config.getCandidateParam().isEmpty()) {
            cfg.set("candidateParam", String.join(",", config.getCandidateParam()));
        }
        cfg.set("ccTiming", "END");
        return buildTextExt("nodeConfig", cfg.toString());
    }

    private Map<String, List<ExtensionElement>> wrapExtMap(List<ExtensionElement> extensions) {
        Map<String, List<ExtensionElement>> map = new HashMap<>();
        for (ExtensionElement ext : extensions) {
            String key = FLOWABLE_NS + ext.getName();
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(ext);
        }
        return map;
    }
}
