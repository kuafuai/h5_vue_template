package com.kuafu.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.constant.FlowComment;
import com.kuafu.flowable.constant.ProcessConstants;
import com.kuafu.flowable.domain.FlowFormDto;
import com.kuafu.flowable.domain.FlowProcDefDto;
import com.kuafu.flowable.factory.FlowServiceFactory;
import com.kuafu.flowable.mapper.FlowDeployMapper;
import com.kuafu.flowable.mapper.FlowFormMapper;
import com.kuafu.flowable.service.IFlowDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class FlowDefinitionServiceImpl extends FlowServiceFactory implements IFlowDefinitionService {

    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Autowired
    private FlowDeployMapper flowDeployMapper;
    @Autowired
    private FlowFormMapper flowFormMapper;

    @Override
    public IPage<FlowProcDefDto> list(String name, Integer current, Integer pageSize) {
        IPage<FlowProcDefDto> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<FlowProcDefDto> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(FlowProcDefDto::getName, name);
        }

        flowDeployMapper.selectPage(page, queryWrapper);

        for (FlowProcDefDto procDef : page.getRecords()) {
            String deployId = procDef.getDeploymentId();
            FlowFormDto formDto = flowFormMapper.selectFormByDeployId(deployId);
            if (Objects.nonNull(formDto)) {
                procDef.setFormName(formDto.getFormName());
                procDef.setFormId(formDto.getFormId());
            }
        }
        return page;
    }

    @Override
    public FlowProcDefDto getLastByName(String name) {
        LambdaQueryWrapper<FlowProcDefDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowProcDefDto::getName, name);
        queryWrapper.orderByDesc(FlowProcDefDto::getDeploymentTime);

        List<FlowProcDefDto> list = flowDeployMapper.selectList(queryWrapper);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void importByFile(String name, String category, InputStream in) {
        Deployment deploy = repositoryService
                .createDeployment()
                .addInputStream(name + BPMN_FILE_SUFFIX, in)
                .name(name)
                .category(category)
                .deploy();

        //查询 流程定义
        ProcessDefinition definition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        repositoryService.setProcessDefinitionCategory(definition.getId(), category);
    }

    @Override
    public void importByString(String name, String category, String content) {
        Deployment deploy = repositoryService
                .createDeployment()
                .addString(name + BPMN_FILE_SUFFIX, content)
                .name(name)
                .category(category)
                .deploy();

        //查询 流程定义
        ProcessDefinition definition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        repositoryService.setProcessDefinitionCategory(definition.getId(), category);
    }

    @Override
    public String readXml(String deployId) throws IOException {
        ProcessDefinition definition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();

        InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(), definition.getResourceName());
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    @Override
    public InputStream readImage(String deployId) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        //获得图片流
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        //输出为图片
        return diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                Collections.emptyList(),
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体",
                null,
                1.0,
                false);
    }

    @Override
    public void delete(String deployId) {
        repositoryService.deleteDeployment(deployId, true);
    }

    @Override
    public void updateState(Integer state, String deployId) {
        ProcessDefinition procDef = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        // 激活
        if (state == 1) {
            repositoryService.activateProcessDefinitionById(procDef.getId(), true, null);
        }
        // 挂起
        if (state == 2) {
            repositoryService.suspendProcessDefinitionById(procDef.getId(), true, null);
        }
    }

    @Override
    public String startProcessInstanceById(String procDefId, Map<String, Object> variables) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(procDefId)
                .latestVersion()
                .singleResult();
        if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
            return null;
        }

        identityService.setAuthenticatedUserId(String.valueOf(SecurityUtils.getUserId()));
        variables.put(ProcessConstants.PROCESS_INITIATOR, SecurityUtils.getUserId());

        // 流程发起时 跳过发起人节点
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
        // 给第一步申请人节点设置任务执行人和意见
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(task)
                && StringUtils.isNotEmpty(task.getAssignee())
                && StringUtils.equalsIgnoreCase(task.getAssignee(), String.valueOf(SecurityUtils.getUserId()))
        ) {
            taskService.addComment(task.getId(), processInstance.getProcessInstanceId(), FlowComment.NORMAL.getType(), "发起流程申请");
            taskService.complete(task.getId(), variables);
        }
        return processInstance.getProcessInstanceId();
    }
}
