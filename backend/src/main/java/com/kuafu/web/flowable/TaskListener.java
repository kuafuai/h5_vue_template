package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.AsyncEventBus;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.domain.FlowProcDefDto;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.qywx.service.QyWxBusinessService;
import com.kuafu.web.entity.*;
import com.kuafu.web.event.TaskCreateEvent;
import com.kuafu.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component("taskListener")
@Slf4j
public class TaskListener {

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private ISubmissionMaterialService submissionMaterialService;


    @Autowired
    private AsyncEventBus asyncEventBus;

    /**
     * 任务创建
     * 给企业微信发送通知
     *
     * @param delegateTask
     */
    public void taskCreate(DelegateTask delegateTask) {
        String taskName = delegateTask.getName();
        String procInsId = delegateTask.getProcessInstanceId();
        //分配人
        String assignee = delegateTask.getAssignee();
        String taskId = delegateTask.getId();

        TaskCreateEvent createEvent = TaskCreateEvent.builder()
                .taskName(taskName)
                .taskId(taskId)
                .assignee(assignee)
                .procInsId(procInsId)
                .build();
        log.info("taskCreate======{},{}", createEvent, delegateTask);
        asyncEventBus.post(createEvent);

    }

    /**
     * 上传提交物 === 创建关联子任务
     *
     * @param delegateTask
     */
    public void subTaskCreate(DelegateTask delegateTask) {

        log.info("subTaskCreate======>{},{},{}", delegateTask, delegateTask.getName(), delegateTask.getVariables());
        processSubTask(delegateTask);
    }

    @Transactional
    public void processSubTask(DelegateTask delegateTask) {
        //subTask
        String parentTaskId = delegateTask.getId();
        String parentProcInsId = delegateTask.getProcessInstanceId();
        Map<String, Object> subTaskMap = (Map<String, Object>) delegateTask.getVariable("subTask");

        String title = subTaskMap.get("title").toString();
        String subTaskAssignee = subTaskMap.get("value").toString();

        LambdaQueryWrapper<SubmissionMaterial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubmissionMaterial::getSubmissionName, title);

        List<SubmissionMaterial> listSub = submissionMaterialService.list(queryWrapper);
        SubmissionMaterial submissionMaterial = listSub.get(0);

        log.info("processSubTask======={}", submissionMaterial);

        String strApproveType = submissionMaterial.getSubmissionType() == 1 ? "Parallel" : "Sequential";
        String flowableName = "SUB_TASK_" + strApproveType;
        FlowProcDefDto flowProcDefDto = flowDefinitionService.getLastByName(flowableName);

        log.info("processSubTask======={}", flowProcDefDto);

        List<String> subUsers = Arrays.asList(StringUtils.split(submissionMaterial.getSubmissionUserId(), ","));

        Map<String, Object> varMaps = Maps.newHashMap();
        varMaps.put("title", title);
        varMaps.put("sub_task_assignee", subTaskAssignee);
        varMaps.put("subUsers", subUsers);

        String procInsId = flowDefinitionService.startProcessInstanceById(flowProcDefDto.getId(), varMaps);

        ChangeManagerSub sub = ChangeManagerSub.builder()
                .parentTaskId(parentTaskId)
                .parentProcInsId(parentProcInsId)
                .subProcInsId(procInsId)
                .build();

        changeManagerSubService.save(sub);
    }

}
