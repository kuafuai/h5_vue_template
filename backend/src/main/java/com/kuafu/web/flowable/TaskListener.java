package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.domain.FlowProcDefDto;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.qywx.service.QyWxBusinessService;
import com.kuafu.web.entity.*;
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
    private IChangeManagerService changeManagerService;
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private QyWxBusinessService qyWxBusinessService;

    @Autowired
    private IChangeTakeRecordService changeTakeRecordService;

    /**
     * 任务创建
     * 给企业微信发送通知
     *
     * @param delegateTask
     */
    public void taskCreate(DelegateTask delegateTask) {
        try {
            String taskName = delegateTask.getName();
            String procInsId = delegateTask.getProcessInstanceId();
            String assignee = delegateTask.getAssignee();

            LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
            parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);
            ChangeManager parentManager = changeManagerService.getOne(parentQuery);
            if (parentManager == null) {
                //说明是子任务
                LambdaQueryWrapper<ChangeManagerSub> subQuery = new LambdaQueryWrapper<>();
                subQuery.eq(ChangeManagerSub::getSubProcInsId, procInsId);
                ChangeManagerSub subTask = changeManagerSubService.getOne(subQuery);
                if (subTask != null) {
                    String parentProcInsId = subTask.getParentProcInsId();

                    parentQuery = new LambdaQueryWrapper<>();
                    parentQuery.eq(ChangeManager::getFlowableInstanceId, parentProcInsId);
                    parentManager = changeManagerService.getOne(parentQuery);
                }
            }

            Long startUserId = Long.parseLong(assignee);
            UserInfo userInfo = userInfoService.getById(startUserId);
            String userId = userInfo.getUserId();
            String title = "变更任务";
            String url = "http://dev.codeflying.net:9090/";
            String description;
            if (parentManager != null) {
                description = "您收到一条：" + parentManager.getChangeProjectName() + "-" + parentManager.getChangeTitle() + "\n"
                        + taskName;
            } else {
                description = "您收到一条：审批变更 任务";
            }

            qyWxBusinessService.sendTextCardMessage(userId, title, description, url);

            LambdaQueryWrapper<ChangeTakeRecord> recordQuery = new LambdaQueryWrapper<>();
            if (parentManager != null) {
                recordQuery.eq(ChangeTakeRecord::getChangeId, parentManager.getChangeId());
                recordQuery.eq(ChangeTakeRecord::getUserId, userInfo.getUserInfoId());

                long count = changeTakeRecordService.count(recordQuery);
                if (count == 0) {
                    ChangeTakeRecord record = ChangeTakeRecord.builder()
                            .changeId(parentManager.getChangeId())
                            .procInsId(parentManager.getFlowableInstanceId())
                            .userId(userInfo.getUserInfoId().intValue())
                            .build();

                    changeTakeRecordService.save(record);
                }
            } else {
                recordQuery.eq(ChangeTakeRecord::getUserId, userInfo.getUserInfoId());
                recordQuery.eq(ChangeTakeRecord::getProcInsId, procInsId);
                long count = changeTakeRecordService.count(recordQuery);
                if (count == 0) {
                    ChangeTakeRecord record = ChangeTakeRecord.builder()
                            .changeId(0)
                            .procInsId(procInsId)
                            .userId(userInfo.getUserInfoId().intValue())
                            .build();

                    changeTakeRecordService.save(record);
                }
            }

        } catch (Exception e) {
            log.error("{}", e);
        }

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
