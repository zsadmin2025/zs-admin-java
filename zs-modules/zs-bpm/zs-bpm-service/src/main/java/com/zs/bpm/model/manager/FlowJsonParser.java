package com.zs.bpm.model.manager;

import cn.hutool.core.util.StrUtil;
import com.zs.bpm.model.domain.dto.*;
import com.zs.common.core.enums.bpmn.FlowableActivityTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FlowJsonParser {

    /**
     * 节点链解析结果：封装首节点ID + 末节点ID
     * 彻底取消按名称匹配节点，从根源解决重名节点连线错乱
     */
    private static class NodeChainResult {
        String firstNodeId;
        String lastNodeId;

        public NodeChainResult(String firstNodeId, String lastNodeId) {
            this.firstNodeId = firstNodeId;
            this.lastNodeId = lastNodeId;
        }
    }

    /**
     * 入口：钉钉树形JSON → 扁平化节点+连线
     * <p>
     * 修复：强制插入标准 startEvent，符合 BPMN 2.0 规范
     * <p>
     * 线程安全：所有状态变量均为方法局部变量，
     * 通过参数在递归方法间传递，消除 Spring 单例并发隐患
     */
    public FlatFlowResult parse(FlowRootDTO root) {
        // 局部变量，消除 Spring 单例并发安全隐患
        AtomicInteger idCounter = new AtomicInteger(0);
        List<FlowNodeDTO> nodes = new ArrayList<>();
        List<FlowEdgeDTO> edges = new ArrayList<>();
        List<String> endNodes = new ArrayList<>();

        // ========== 新增：强制生成标准开始事件 ==========
        String startEventId = "start_" + idCounter.incrementAndGet();
        nodes.add(new FlowNodeDTO(startEventId, FlowableActivityTypeEnum.START_EVENT.getValue(), "开始"));

        // 1. 递归解析主业务链路，得到首尾节点ID
        NodeChainResult mainChain = parseNodeChain(root.getNodeConfig(), idCounter, nodes, edges, endNodes);
        endNodes.add(mainChain.lastNodeId);

        // 2. 开始事件 → 主链路第一个节点 连线
        edges.add(new FlowEdgeDTO(
                "flow_" + idCounter.incrementAndGet(),
                startEventId,
                mainChain.firstNodeId,
                null
        ));

        // 3. 补充全局结束事件
        String endId = "end_" + idCounter.incrementAndGet();
        nodes.add(new FlowNodeDTO(endId, FlowableActivityTypeEnum.END_EVENT.getValue(), "结束"));

        // 4. 所有末端节点统一连接到结束事件（去重）
        endNodes.stream().distinct().forEach(lastNodeId ->
                edges.add(new FlowEdgeDTO("flow_" + idCounter.incrementAndGet(), lastNodeId, endId, null))
        );

        return new FlatFlowResult(root.getWorkFlowDef().getName(), nodes, edges);
    }


    /**
     * 递归解析节点链，返回首尾节点ID
     * 所有节点类型统一入口，保证逻辑闭环
     *
     * @param node      当前节点配置
     * @param idCounter 全局ID计数器（AtomicInteger 保证递归间共享可变状态）
     * @param nodes     节点列表（累积收集）
     * @param edges     连线列表（累积收集）
     * @param endNodes  末端节点ID列表（用于最终连接结束事件）
     */
    private NodeChainResult parseNodeChain(NodeConfigDTO node, AtomicInteger idCounter,
            List<FlowNodeDTO> nodes, List<FlowEdgeDTO> edges, List<String> endNodes) {
        int nodeId = idCounter.incrementAndGet();
        return switch (node.getType()) {
            case 0 -> parseStartUserNode(node, nodeId, idCounter, nodes, edges, endNodes);
            case 1 -> parseUserTaskNode(node, nodeId, idCounter, nodes, edges, endNodes);
            case 2 -> parseCcNode(node, nodeId, idCounter, nodes, edges, endNodes);
            case 4 -> parseGatewayNode(node, nodeId, idCounter, nodes, edges, endNodes);
            default -> throw new IllegalArgumentException("未知节点类型: " + node.getType());
        };
    }

    // 发起人节点 → UserTask
    private NodeChainResult parseStartUserNode(NodeConfigDTO node, int nodeId, AtomicInteger idCounter,
            List<FlowNodeDTO> nodes, List<FlowEdgeDTO> edges, List<String> endNodes) {
        String id = "initiator_" + nodeId;
        nodes.add(new FlowNodeDTO(id, FlowableActivityTypeEnum.USER_TASK.getValue(), node.getNodeName(), node));

        String firstId = id;
        String lastId = id;

        if (node.getChildNode() != null) {
            NodeChainResult nextChain = parseNodeChain(node.getChildNode(), idCounter, nodes, edges, endNodes);
            edges.add(new FlowEdgeDTO("flow_" + idCounter.incrementAndGet(), id, nextChain.firstNodeId, null));
            lastId = nextChain.lastNodeId;
        }

        return new NodeChainResult(firstId, lastId);
    }

    // 审批人节点 → UserTask
    private NodeChainResult parseUserTaskNode(NodeConfigDTO node, int nodeId, AtomicInteger idCounter,
            List<FlowNodeDTO> nodes, List<FlowEdgeDTO> edges, List<String> endNodes) {
        String id = "task_" + nodeId;
        nodes.add(new FlowNodeDTO(id, FlowableActivityTypeEnum.USER_TASK.getValue(), node.getNodeName(), node));

        String firstId = id;
        String lastId = id;

        if (node.getChildNode() != null) {
            NodeChainResult nextChain = parseNodeChain(node.getChildNode(), idCounter, nodes, edges, endNodes);
            edges.add(new FlowEdgeDTO("flow_" + idCounter.incrementAndGet(), id, nextChain.firstNodeId, null));
            lastId = nextChain.lastNodeId;
        }

        return new NodeChainResult(firstId, lastId);
    }

    // 抄送人节点 → ServiceTask
    private NodeChainResult parseCcNode(NodeConfigDTO node, int nodeId, AtomicInteger idCounter,
            List<FlowNodeDTO> nodes, List<FlowEdgeDTO> edges, List<String> endNodes) {
        String id = "cc_" + nodeId;
        nodes.add(new FlowNodeDTO(id, FlowableActivityTypeEnum.SERVICE_TASK.getValue(), node.getNodeName(), node));

        String firstId = id;
        String lastId = id;

        if (node.getChildNode() != null) {
            NodeChainResult nextChain = parseNodeChain(node.getChildNode(), idCounter, nodes, edges, endNodes);
            edges.add(new FlowEdgeDTO("flow_" + idCounter.incrementAndGet(), id, nextChain.firstNodeId, null));
            lastId = nextChain.lastNodeId;
        }

        return new NodeChainResult(firstId, lastId);
    }

    // 路由 → 成对排他网关（分支网关+汇聚网关）
    private NodeChainResult parseGatewayNode(NodeConfigDTO node, int nodeId, AtomicInteger idCounter,
            List<FlowNodeDTO> nodes, List<FlowEdgeDTO> edges, List<String> endNodes) {
        // 1. 创建分支网关（入口）
        String splitGatewayId = "gateway_split_" + nodeId;
        FlowNodeDTO splitGateway = new FlowNodeDTO(splitGatewayId, FlowableActivityTypeEnum.EXCLUSIVE_GATEWAY.getValue(), node.getNodeName());
        nodes.add(splitGateway);

        // 2. 遍历所有条件分支，解析每个分支的节点链
        List<String> branchEndIds = new ArrayList<>();
        for (ConditionNodeDTO condition : node.getConditionNodes()) {
            NodeChainResult branchChain = parseNodeChain(condition.getChildNode(), idCounter, nodes, edges, endNodes);
            branchEndIds.add(branchChain.lastNodeId);

            // 构建条件配置
            ConditionConfigDTO conditionConfig = null;
            if (!Boolean.TRUE.equals(condition.getIsOther())) {
                conditionConfig = buildConditionConfig(condition);
            }

            // 分支网关 → 分支首节点 连线（ID精准匹配，4参数严格对齐）
            FlowEdgeDTO edge = new FlowEdgeDTO(
                    "flow_" + idCounter.incrementAndGet(),
                    splitGatewayId,
                    branchChain.firstNodeId,
                    conditionConfig
            );
            edges.add(edge);

            // 标记默认分支
            if (Boolean.TRUE.equals(condition.getIsOther())) {
                splitGateway.setDefaultFlowId(edge.getId());
            }
        }

        String firstId = splitGatewayId;
        String lastId;

        // 3. 处理汇聚逻辑
        if (node.getChildNode() != null) {
            // 有后续公共节点：排他网关无需汇聚网关，各分支末端直接连到后续节点链首节点
            NodeChainResult nextChain = parseNodeChain(node.getChildNode(), idCounter, nodes, edges, endNodes);

            // 所有分支末端直接连到后续节点链首节点（符合 BPMN 2.0 排他网关最佳实践）
            branchEndIds.forEach(endId ->
                    edges.add(new FlowEdgeDTO("flow_" + idCounter.incrementAndGet(), endId, nextChain.firstNodeId, null))
            );

            // 整条网关链的末端 = 后续节点链的末端
            lastId = nextChain.lastNodeId;
        } else {
            // 无后续节点：所有分支末端加入全局列表，由入口统一连结束事件
            endNodes.addAll(branchEndIds);
            lastId = branchEndIds.get(0);
        }

        return new NodeChainResult(firstId, lastId);
    }

    /**
     * 构建条件配置对象
     * <p>
     * 支持两种方式：
     * 1. expression模式：直接使用原始EL表达式（configMode="expression"）
     * 2. conditionList模式：通过条件项列表构建（默认）
     */
    private ConditionConfigDTO buildConditionConfig(ConditionNodeDTO condition) {
        ConditionConfigDTO config = new ConditionConfigDTO();

        // 方式1：expression模式，直接使用原始EL表达式
        if ("expression".equals(condition.getConfigMode()) && StrUtil.isNotBlank(condition.getExpression())) {
            config.setExpression(condition.getExpression());
            return config;
        }

        // 方式2：从conditionList构建
        config.setLogic("AND");
        config.setConditions(condition.getConditionList());
        config.setIsOther(condition.getIsOther());
        return config;
    }
}
