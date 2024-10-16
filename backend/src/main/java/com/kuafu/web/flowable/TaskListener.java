package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.entity.SubmissionMaterial;
import com.kuafu.web.service.IChangeManagerSubService;
import com.kuafu.web.service.ISubmissionMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component("taskListener")
@Slf4j
public class TaskListener {

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private ISubmissionMaterialService submissionMaterialService;

    //todo 关联的子任务 流程ID
    private String subTaskProcDefId = "sub_task:2:47504";

    /**
     * 任务创建
     * 给企业微信发送通知
     *
     * @param delegateTask
     */
    public void taskCreate(DelegateTask delegateTask) {

        log.info("TaskListener======>{},{},{}", delegateTask.getAssignee(), delegateTask.getName(), delegateTask.getVariables());
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
        List<String> subUsers = Arrays.asList(StringUtils.split(submissionMaterial.getSubmissionUserId(), ","));

        Map<String, Object> varMaps = Maps.newHashMap();
        varMaps.put("title", title);
        varMaps.put("sub_task_assignee", subTaskAssignee);
        varMaps.put("subUsers", subUsers);

        String procInsId = flowDefinitionService.startProcessInstanceById(subTaskProcDefId, varMaps);

        ChangeManagerSub sub = ChangeManagerSub.builder()
                .parentTaskId(parentTaskId)
                .parentProcInsId(parentProcInsId)
                .subProcInsId(procInsId)
                .build();

        changeManagerSubService.save(sub);
    }

}
