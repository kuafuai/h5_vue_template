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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {MisAppBackendApplication.class})
@RunWith(SpringRunner.class)
@Slf4j
public class EmployeeTest {


    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private ChangeManagerBusinessService changeManagerBusinessService;

    @Test
    public void test1() {

    }

    @Test
    public void test2() {
        flowDefinitionService.importByString("SUB_TASK", "SUB_TASK", getText());
    }

    @Test
    public void test_select() {
        log.info("{}", flowDefinitionService.list("d", 1, 10));
    }


    @Test
    public void test3() {
        System.out.println(AppConfig.getProfile());
    }

    @Test
    public void test_flow() {
        log.info("{}", JSON.toJSONString(changeManagerBusinessService.getChangeFlowRecords("12622")));
    }

    public String getText() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:bioc=\"http://bpmn.io/schema/bpmn/biocolor/1.0\" xmlns:flowable=\"http://flowable.org/bpmn\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://www.flowable.org/processdef\">\n" +
                "  <process id=\"sub_task\" name=\"提交物流程\">\n" +
                "    <startEvent id=\"start_event\" name=\"开始\">\n" +
                "      <outgoing>Flow_0wisgij</outgoing>\n" +
                "    </startEvent>\n" +
                "    <userTask id=\"Activity_1hd4cqp\" name=\"${title}\" flowable:assignee=\"${sub_task_assignee}\" flowable:expId=\"6\">\n" +
                "      <extensionElements>\n" +
                "        <flowable:taskListener expression=\"${taskListener.taskCreate(task)}\" event=\"create\" />\n" +
                "      </extensionElements>\n" +
                "      <incoming>Flow_0wisgij</incoming>\n" +
                "      <outgoing>Flow_1nu8qcy</outgoing>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_0wisgij\" sourceRef=\"start_event\" targetRef=\"Activity_1hd4cqp\" />\n" +
                "    <userTask id=\"Activity_1i8r46j\" name=\"审核\" flowable:assignee=\"${subUser}\">\n" +
                "      <extensionElements>\n" +
                "        <flowable:taskListener expression=\"${taskListener.taskCreate(task)}\" event=\"create\" />\n" +
                "      </extensionElements>\n" +
                "      <incoming>Flow_1nu8qcy</incoming>\n" +
                "      <outgoing>Flow_1uav5kw</outgoing>\n" +
                "      <multiInstanceLoopCharacteristics isSequential=\"true\" flowable:collection=\"subUsers\" flowable:elementVariable=\"subUser\">\n" +
                "        <completionCondition>${taskBusinessService.completeSubmitNode(execution)}</completionCondition>\n" +
                "      </multiInstanceLoopCharacteristics>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_1nu8qcy\" sourceRef=\"Activity_1hd4cqp\" targetRef=\"Activity_1i8r46j\" />\n" +
                "    <endEvent id=\"Event_0shvw8k\">\n" +
                "      <incoming>Flow_1uav5kw</incoming>\n" +
                "    </endEvent>\n" +
                "    <sequenceFlow id=\"Flow_1uav5kw\" sourceRef=\"Activity_1i8r46j\" targetRef=\"Event_0shvw8k\" />\n" +
                "  </process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_flow\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_flow\" bpmnElement=\"sub_task\">\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_start_event\" bpmnElement=\"start_event\" bioc:stroke=\"\">\n" +
                "        <omgdc:Bounds x=\"105\" y=\"155\" width=\"30\" height=\"30\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds x=\"108\" y=\"192\" width=\"22\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1hd4cqp_di\" bpmnElement=\"Activity_1hd4cqp\">\n" +
                "        <omgdc:Bounds x=\"190\" y=\"130\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1i8r46j_di\" bpmnElement=\"Activity_1i8r46j\">\n" +
                "        <omgdc:Bounds x=\"350\" y=\"130\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_0shvw8k_di\" bpmnElement=\"Event_0shvw8k\">\n" +
                "        <omgdc:Bounds x=\"512\" y=\"152\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0wisgij_di\" bpmnElement=\"Flow_0wisgij\">\n" +
                "        <di:waypoint x=\"135\" y=\"170\" />\n" +
                "        <di:waypoint x=\"190\" y=\"170\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1nu8qcy_di\" bpmnElement=\"Flow_1nu8qcy\">\n" +
                "        <di:waypoint x=\"290\" y=\"170\" />\n" +
                "        <di:waypoint x=\"350\" y=\"170\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_1uav5kw_di\" bpmnElement=\"Flow_1uav5kw\">\n" +
                "        <di:waypoint x=\"450\" y=\"170\" />\n" +
                "        <di:waypoint x=\"512\" y=\"170\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</definitions>\n";
    }

    public String getText1() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:bioc=\"http://bpmn.io/schema/bpmn/biocolor/1.0\" xmlns:flowable=\"http://flowable.org/bpmn\" xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://www.flowable.org/processdef\">\n" +
                "  <process id=\"测试表单07\" name=\"测试表单07\">\n" +
                "    <startEvent id=\"start_event\" name=\"开始\">\n" +
                "      <outgoing>Flow_058ch1c</outgoing>\n" +
                "    </startEvent>\n" +
                "    <userTask id=\"Activity_1amgijq\" name=\"第一步\" flowable:assignee=\"${INITIATOR}\" flowable:dataType=\"fixed\" flowable:expId=\"1\">\n" +
                "      <incoming>Flow_058ch1c</incoming>\n" +
                "      <outgoing>Flow_01kwdwr</outgoing>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_058ch1c\" sourceRef=\"start_event\" targetRef=\"Activity_1amgijq\" />\n" +
                "    <userTask id=\"Activity_0hv0fnd\" name=\"第二步\" flowable:formKey=\"2\" flowable:assignee=\"${assignee}\" flowable:dataType=\"fixed\">\n" +
                "      <extensionElements>\n" +
                "        <flowable:taskListener expression=\"${taskListener.taskCreate(task,task.eventName)}\" event=\"create\" />\n" +
                "        <flowable:taskListener expression=\"${taskListener.taskAssignment(task)}\" event=\"assignment\" />\n" +
                "      </extensionElements>\n" +
                "      <incoming>Flow_01kwdwr</incoming>\n" +
                "      <outgoing>Flow_0hq5zpu</outgoing>\n" +
                "      <multiInstanceLoopCharacteristics isSequential=\"${isSequential == 'true'}\" flowable:collection=\"assigneeList\" flowable:elementVariable=\"assignee\" >\n" +
                "        <completionCondition>${multiInstanceCompleteTask.completeTaskTo(execution)}</completionCondition>\n" +
                "      </multiInstanceLoopCharacteristics>\n" +
                "    </userTask>\n" +
                "    <sequenceFlow id=\"Flow_01kwdwr\" sourceRef=\"Activity_1amgijq\" targetRef=\"Activity_0hv0fnd\" />\n" +
                "    <endEvent id=\"Event_0drew3k\">\n" +
                "      <incoming>Flow_0hq5zpu</incoming>\n" +
                "    </endEvent>\n" +
                "    <sequenceFlow id=\"Flow_0hq5zpu\" sourceRef=\"Activity_0hv0fnd\" targetRef=\"Event_0drew3k\" />\n" +
                "  </process>\n" +
                "  <bpmndi:BPMNDiagram id=\"BPMNDiagram_flow\">\n" +
                "    <bpmndi:BPMNPlane id=\"BPMNPlane_flow\" bpmnElement=\"测试表单\">\n" +
                "      <bpmndi:BPMNShape id=\"BPMNShape_start_event\" bpmnElement=\"start_event\" bioc:stroke=\"\">\n" +
                "        <omgdc:Bounds x=\"55\" y=\"105\" width=\"30\" height=\"30\" />\n" +
                "        <bpmndi:BPMNLabel>\n" +
                "          <omgdc:Bounds x=\"58\" y=\"142\" width=\"22\" height=\"14\" />\n" +
                "        </bpmndi:BPMNLabel>\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_1amgijq_di\" bpmnElement=\"Activity_1amgijq\">\n" +
                "        <omgdc:Bounds x=\"140\" y=\"80\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Activity_0hv0fnd_di\" bpmnElement=\"Activity_0hv0fnd\">\n" +
                "        <omgdc:Bounds x=\"300\" y=\"80\" width=\"100\" height=\"80\" />\n" +
                "        <bpmndi:BPMNLabel />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNShape id=\"Event_0drew3k_di\" bpmnElement=\"Event_0drew3k\">\n" +
                "        <omgdc:Bounds x=\"462\" y=\"102\" width=\"36\" height=\"36\" />\n" +
                "      </bpmndi:BPMNShape>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_058ch1c_di\" bpmnElement=\"Flow_058ch1c\">\n" +
                "        <di:waypoint x=\"85\" y=\"120\" />\n" +
                "        <di:waypoint x=\"140\" y=\"120\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_01kwdwr_di\" bpmnElement=\"Flow_01kwdwr\">\n" +
                "        <di:waypoint x=\"240\" y=\"120\" />\n" +
                "        <di:waypoint x=\"300\" y=\"120\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "      <bpmndi:BPMNEdge id=\"Flow_0hq5zpu_di\" bpmnElement=\"Flow_0hq5zpu\">\n" +
                "        <di:waypoint x=\"400\" y=\"120\" />\n" +
                "        <di:waypoint x=\"462\" y=\"120\" />\n" +
                "      </bpmndi:BPMNEdge>\n" +
                "    </bpmndi:BPMNPlane>\n" +
                "  </bpmndi:BPMNDiagram>\n" +
                "</definitions>\n";
    }
}
