package com.kuafu.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.MisAppBackendApplication;
import com.kuafu.common.config.AppConfig;
import com.kuafu.common.util.JSON;
import com.kuafu.flowable.domain.FlowProcDefDto;
import com.kuafu.flowable.mapper.FlowDeployMapper;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.web.flowable.ChangeManagerBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest(classes = {MisAppBackendApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class EmployeeTest {


    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private ChangeManagerBusinessService changeManagerBusinessService;

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void test1() {

    }

    @Test
    public void test2() {
        flowDefinitionService.importByString("ECR_Parallel", "ECR", getText());
    }

    @Test
    public void test_select() {
        log.info("{}", flowDefinitionService.list("d", 1, 10));
    }


    @Test
    public void test3() throws IOException {
        //流程定义
        ProcessDefinition definition =
                repositoryService.createProcessDefinitionQuery().deploymentId("20001").singleResult();
        InputStream inputStream =
                repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        log.info("{}, {}, {},{}", definition, definition.getResourceName(), definition.getDiagramResourceName(), result);
    }

    @Test
    public void test4() throws IOException {
        ProcessDefinition definition =
                repositoryService.createProcessDefinitionQuery().deploymentId("20001").singleResult();
        //获得图片流
        InputStream inputStream =
                repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getDiagramResourceName());
//        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(definition.getId());
//
//        InputStream inputStream = diagramGenerator.generateDiagram(
//                bpmnModel,
//                "png",
//                Collections.emptyList(),
//                Collections.emptyList(),
//                "宋体",
//                "宋体",
//                "宋体",
//                null,
//                1.0,
//                false);
//
        IOUtils.copy(inputStream, new FileOutputStream("test1.png"));
    }

    @Test
    public void test_flow() {
        log.info("{}", JSON.toJSONString(changeManagerBusinessService.getChangeFlowRecords("12622", false)));
    }

    public String getText() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:bioc=\"http://bpmn.io/schema/bpmn/biocolor/1.0\" xmlns:flowable=\"http://flowable.org/bpmn\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://www.flowable.org/processdef\">\n" +
                "  <process id=\"ECR_Parallel\" name=\"ECR_Parallel\">\n" +
                "    <extensionElements>\n" +
                "      <flowable:executionListener expression=\"${taskBusinessService.flowableEnd(execution)}\" event=\"end\" />\n" +
                "    </extensionElements>\n" +
                "    <startEvent id=\"start_event\" name=\"开始\">\n" +
                "      <outgoing>Flow_1a6ed1m</outgoing>\n" +
                "    </startEvent>\n" +
                "    <userTask id=\"提交变更申请\" name=\"提交变更申请\" flowable:assignee=\"${INITIATOR}\" flowable:dataType=\"fixed\" flowable:expId=\"1\">\n" +
                "      <incoming>Flow_1a6ed1m</incoming>\n" +
                "      <outgoing>Flow_0s3bs64</outgoing>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_1a6ed1m\" sourceRef=\"start_event\" targetRef=\"提交变更申请\" />\n" +
                "    <userTask id=\"审批变更\" name=\"审批变更\" flowable:assignee=\"${assignee}\">\n" +
                "      <extensionElements>\n" +
                "        <flowable:taskListener expression=\"${taskListener.taskCreate(task)}\" event=\"create\" />\n" +
                "      </extensionElements>\n" +
                "      <incoming>Flow_0s3bs64</incoming>\n" +
                "      <outgoing>Flow_0rge6ze</outgoing>\n" +
                "      <multiInstanceLoopCharacteristics isSequential=\"false\" flowable:collection=\"${taskBusinessService.getUsersByApproveNode()}\" flowable:elementVariable=\"assignee\">\n" +
                "        <completionCondition>${taskBusinessService.completeApproveNode(execution)}</completionCondition>\n" +
                "      </multiInstanceLoopCharacteristics>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_0s3bs64\" sourceRef=\"提交变更申请\" targetRef=\"审批变更\" />\n" +
                "    <userTask id=\"Activity_1dgi1dv\" name=\"指定输出提交物\" flowable:assignee=\"${INITIATOR}\" flowable:dataType=\"fixed\" flowable:expId=\"1\">\n" +
                "      <incoming>Flow_0rge6ze</incoming>\n" +
                "      <outgoing>Flow_06kvxgd</outgoing>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_0rge6ze\" sourceRef=\"审批变更\" targetRef=\"Activity_1dgi1dv\" />\n" +
                "    <userTask id=\"Activity_16t4ea2\" name=\"上传提交物\">\n" +
                "      <extensionElements>\n" +
                "        <flowable:taskListener expression=\"${taskListener.subTaskCreate(task)}\" event=\"create\" />\n" +
                "      </extensionElements>\n" +
                "      <incoming>Flow_06kvxgd</incoming>\n" +
                "      <outgoing>Flow_0r2xaat</outgoing>\n" +
                "      <multiInstanceLoopCharacteristics flowable:collection=\"subTasks\" flowable:elementVariable=\"subTask\">\n" +
                "        <completionCondition>${taskBusinessService.completeSettingSubmitNode(execution)}</completionCondition>\n" +
                "      </multiInstanceLoopCharacteristics>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_06kvxgd\" sourceRef=\"Activity_1dgi1dv\" targetRef=\"Activity_16t4ea2\" />\n" +
                "    <userTask id=\"Activity_05iriaa\" name=\"上传验收报告\" flowable:assignee=\"${INITIATOR}\" flowable:dataType=\"fixed\" flowable:expId=\"1\">\n" +
                "      <incoming>Flow_0r2xaat</incoming>\n" +
                "      <outgoing>Flow_1tj4wt8</outgoing>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_0r2xaat\" sourceRef=\"Activity_16t4ea2\" targetRef=\"Activity_05iriaa\" />\n" +
                "    <endEvent id=\"Event_0chsgs6\">\n" +
                "      <incoming>Flow_1tj4wt8</incoming>\n" +
                "    </endEvent>\n" +
                "    <sequenceFlow id=\"Flow_1tj4wt8\" sourceRef=\"Activity_05iriaa\" targetRef=\"Event_0chsgs6\" />\n" +
                "  </process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_flow\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_flow\" bpmnElement=\"ECR\">\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_start_event\" bpmnElement=\"start_event\" bioc:stroke=\"\">\n" +
                "        <omgdc:Bounds x=\"55\" y=\"85\" width=\"30\" height=\"30\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds x=\"58\" y=\"122\" width=\"22\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0dqtwc9_di\" bpmnElement=\"提交变更申请\">\n" +
                "        <omgdc:Bounds x=\"140\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0axk859_di\" bpmnElement=\"审批变更\">\n" +
                "        <omgdc:Bounds x=\"300\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1dgi1dv_di\" bpmnElement=\"Activity_1dgi1dv\">\n" +
                "        <omgdc:Bounds x=\"460\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_16t4ea2_di\" bpmnElement=\"Activity_16t4ea2\">\n" +
                "        <omgdc:Bounds x=\"620\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_05iriaa_di\" bpmnElement=\"Activity_05iriaa\">\n" +
                "        <omgdc:Bounds x=\"780\" y=\"60\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_0chsgs6_di\" bpmnElement=\"Event_0chsgs6\">\n" +
                "        <omgdc:Bounds x=\"942\" y=\"82\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1a6ed1m_di\" bpmnElement=\"Flow_1a6ed1m\">\n" +
                "        <di:waypoint x=\"85\" y=\"100\" />\n" +
                "        <di:waypoint x=\"140\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0s3bs64_di\" bpmnElement=\"Flow_0s3bs64\">\n" +
                "        <di:waypoint x=\"240\" y=\"100\" />\n" +
                "        <di:waypoint x=\"300\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0rge6ze_di\" bpmnElement=\"Flow_0rge6ze\">\n" +
                "        <di:waypoint x=\"400\" y=\"100\" />\n" +
                "        <di:waypoint x=\"460\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_06kvxgd_di\" bpmnElement=\"Flow_06kvxgd\">\n" +
                "        <di:waypoint x=\"560\" y=\"100\" />\n" +
                "        <di:waypoint x=\"620\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0r2xaat_di\" bpmnElement=\"Flow_0r2xaat\">\n" +
                "        <di:waypoint x=\"720\" y=\"100\" />\n" +
                "        <di:waypoint x=\"780\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1tj4wt8_di\" bpmnElement=\"Flow_1tj4wt8\">\n" +
                "        <di:waypoint x=\"880\" y=\"100\" />\n" +
                "        <di:waypoint x=\"942\" y=\"100\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</definitions>\n";
    }

}
