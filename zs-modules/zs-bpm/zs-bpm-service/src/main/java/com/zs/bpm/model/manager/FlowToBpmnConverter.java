package com.zs.bpm.model.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.zs.bpm.model.domain.dto.FlatFlowResult;
import com.zs.bpm.model.domain.dto.FlowRootDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * 前端流程设计器JSON转BPMN XML转换器
 * <p>
 * 将钉钉设计器JSON格式的流程定义转换为BPMN 2.0标准XML。
 * 实际转换逻辑委托给 {@link FlowJsonToBpmnUtil} 工具类。
 * <p>
 * 节点类型映射：
 * <ul>
 *   <li>type=0 发起人 → UserTask（仅 assignee，代码手动完成）</li>
 *   <li>type=1 审批人 → UserTask + multiInstanceLoopCharacteristics + extensionElements</li>
 *   <li>type=2 抄送人 → ServiceTask + delegateExpression</li>
 *   <li>type=3 条件   → 条件分支SequenceFlow</li>
 *   <li>type=4 路由   → ExclusiveGateway（仅入口，无合并网关）</li>
 * </ul>
 * <p>
 * 扩展配置存储在 extensionElements 子元素中，使用 BpmnAutoLayout 自动布局，
 * 运行时由 {@code BusinessAssigneeLoader} 动态解析审批人。
 *
 * @author zsadmin
 */
@Slf4j
@Component
public class FlowToBpmnConverter {

    @Resource
    private FlowJsonParser flowJsonParser;

    @Resource
    private BpmnXmlGenerator bpmnXmlGenerator;

    /**
     * 将前端JSON流程定义转换为BPMN XML字符串
     *
     * @param flowJson    前端JSON格式的流程定义
     * @param processKey  流程唯一标识
     * @param processName 流程名称
     * @return BPMN 2.0 XML字符串
     */
    public String convertToBpmn(String flowJson, String processKey, String processName) {
        if (StrUtil.isBlank(flowJson)) {
            log.warn("前端JSON流程定义为空");
            return null;
        }

        try {
            FlowRootDTO root = JSONUtil.toBean(flowJson, FlowRootDTO.class);
            FlatFlowResult flatResult = flowJsonParser.parse(root);
            BpmnModel bpmnModel = bpmnXmlGenerator.generate(flatResult, processKey, processName);

            // 4. 转换为XML字符串
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] xmlBytes = xmlConverter.convertToXML(bpmnModel);
            String xml = new String(xmlBytes, StandardCharsets.UTF_8);

            return xml;
            // 委托给FlowJsonToBpmnUtil进行转换
           // return FlowJsonToBpmnUtil.jsonToBpmnXml(flowJson, processKey, processName);
        } catch (Exception e) {
            log.error("转换前端JSON到BPMN XML失败: processKey={}", processKey, e);
            return null;
        }
    }

    /**
     * 从BPMN XML字符串解析BpmnModel
     *
     * @param bpmnXml BPMN XML字符串
     * @return BpmnModel对象
     */
    public BpmnModel getBpmnModel(String bpmnXml) {
        if (StrUtil.isBlank(bpmnXml)) {
            return new BpmnModel();
        }
        try {
            BpmnXMLConverter converter = new BpmnXMLConverter();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(
                new ByteArrayInputStream(bpmnXml.getBytes(StandardCharsets.UTF_8))
            );
            return converter.convertToBpmnModel(reader);
        } catch (Exception e) {
            log.error("解析BPMN XML失败", e);
            return new BpmnModel();
        }
    }

    /**
     * 将BpmnModel转换为前端JSON格式
     *
     * @param bpmnModel BpmnModel对象
     * @return 前端JSON格式的流程定义
     */
    public String convertToJson(BpmnModel bpmnModel) {
        if (bpmnModel == null) {
            return null;
        }
        try {
            return FlowJsonToBpmnUtil.bpmnToFlowJson(bpmnModel).toString();
        } catch (Exception e) {
            log.error("转换BpmnModel到前端JSON失败", e);
            return null;
        }
    }
}
