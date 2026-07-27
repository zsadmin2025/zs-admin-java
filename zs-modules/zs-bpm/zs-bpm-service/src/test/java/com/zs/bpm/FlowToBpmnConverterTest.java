package com.zs.bpm;

import cn.hutool.core.util.StrUtil;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.model.manager.FlowToBpmnConverter;
import com.zs.bpm.model.service.IBpmProcessModelService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.util.io.StringStreamSource;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * FlowToBpmnConverter 测试
 *
 * @author zsadmin
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FlowToBpmnConverterTest.TestConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FlowToBpmnConverterTest {

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan("com.zs.bpm")
    static class TestConfig {
    }

    @Resource
    private FlowToBpmnConverter flowToBpmnConverter;

    @Resource
    private IBpmProcessModelService bpmProcessModelService;

    @Test
    public void testConvertToBpmn() {

        BpmProcessDefinitionInfoVO bpmProcessDefinitionInfoVO = bpmProcessModelService.getDetail(2072306806228316161L);
//        log.info("流程定义信息VO: {}", bpmProcessDefinitionInfoVO);
        String flowJson = bpmProcessDefinitionInfoVO.getModelJson();
        String processKey = "leave_process";
        String processName = "请假流程";

        String bpmnXml = flowToBpmnConverter.convertToBpmn(flowJson, processKey, processName);
        log.info("生成的BPMN XML:\n{}", bpmnXml);

        // 验证XML结构（标准BPMN 2.0）
        assert bpmnXml.contains("<startEvent") : "应该包含startEvent";
        assert bpmnXml.contains("<userTask") : "应该包含userTask";
        assert bpmnXml.contains("<exclusiveGateway") : "应该包含exclusiveGateway";
        assert bpmnXml.contains("<endEvent") : "应该包含endEvent";
        assert bpmnXml.contains("<serviceTask") : "应该包含serviceTask（抄送人）";
        assert bpmnXml.contains("leave_process") : "应该包含processKey";
        assert bpmnXml.contains("请假流程") : "应该包含processName";
        assert bpmnXml.contains("${Ffgumqup2456aec < 3}") : "条件表达式格式应正确";
        assert bpmnXml.contains("default=") : "网关应该有default属性";



        log.info("所有断言通过！");

        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(new StringStreamSource(bpmnXml), false, false);

        byte[] bytes = bpmnXMLConverter.convertToXML(bpmnModel);
        String xml = StrUtil.utf8Str(bytes);
//        log.info("XML:\n{}", xml);

    }

    @Test
    public void testSimpleFlow() {

        BpmProcessDefinitionInfoVO bpmProcessDefinitionInfoVO = bpmProcessModelService.getDetail(2072306806228316161L);
        log.info("流程定义信息VO: {}", bpmProcessDefinitionInfoVO);
        String flowJson = bpmProcessDefinitionInfoVO.getModelJson();
        String bpmnXml = flowToBpmnConverter.convertToBpmn(flowJson, "simple_process", "简单流程");
        log.info("简单流程BPMN XML:\n{}", bpmnXml);

        // 验证标准BPMN 2.0元素
        assert bpmnXml.contains("<startEvent") : "应该包含startEvent";
        assert bpmnXml.contains("<userTask") : "应该包含userTask";
        assert bpmnXml.contains("<endEvent") : "应该包含endEvent";
        assert bpmnXml.contains("simple_process") : "应该包含processKey";

        // 验证可以被BpmnModel解析
        org.flowable.bpmn.model.BpmnModel bpmnModel = flowToBpmnConverter.getBpmnModel(bpmnXml);
        assert bpmnModel != null : "BpmnModel应该不为null";
        assert bpmnModel.getMainProcess() != null : "主流程应该不为null";
        assert "simple_process".equals(bpmnModel.getMainProcess().getId()) : "流程ID应该正确";

        log.info("所有断言通过！");
    }
}
