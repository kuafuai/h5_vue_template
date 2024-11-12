package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.util.DateUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.constant.FLowStop;
import com.kuafu.flowable.domain.FlowTaskDto;
import com.kuafu.flowable.domain.FlowTaskVo;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerInfo;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.exception.PersonException;
import com.kuafu.web.service.IChangeManagerInfoService;
import com.kuafu.web.service.IChangeManagerService;
import com.kuafu.web.service.IChangeManagerSubService;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.vo.ChangeManagerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChangeManagerBusinessService {

    @Autowired
    private IChangeManagerService changeManagerService;

    @Autowired
    private IFlowTaskService flowTaskService;

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private IChangeManagerInfoService changeManagerInfoService;

    @Transactional
    public boolean processAddChangeManager(ChangeManagerVO changeManagerVO) {
        String deployId = changeManagerVO.getDeployId();
        String procDefId = changeManagerVO.getProcDefId();
        Map<String, Object> variables = Maps.newHashMap();
        variables.putAll(changeManagerVO.getVariables());

        String procInsId = flowDefinitionService.startProcessInstanceById(procDefId, variables);

        UserInfo currentUserInfo = userInfoService.getById(SecurityUtils.getUserId());

        ChangeManager changeManager = ChangeManager.builder()
                .changeType(changeManagerVO.getChangeType())
                .changeTitle(changeManagerVO.getChangeTitle())
                .changeCustomer(changeManagerVO.getChangeCustomer())
                .changeProjectName(changeManagerVO.getChangeProjectName())
                .changeProductName(changeManagerVO.getChangeProductName())
                .changeStartTime(DateUtils.getNowDate())
                .changeEndTime(changeManagerVO.getChangeEndTime())
                .changePerson(currentUserInfo.getUserInfoId().toString())
                .changePersonName(currentUserInfo.getUserName())
                .changeProjectStage(changeManagerVO.getChangeProjectStage())
                .partNumber(changeManagerVO.getPartNumber())
                .flowableInstanceId(procInsId)
                .build();

        boolean flag = changeManagerService.save(changeManager);
        List<ChangeManagerInfo> infoList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : changeManagerVO.getVariables().entrySet()) {
            if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                Object value = entry.getValue();

                if (value instanceof String) {
                    ChangeManagerInfo info = ChangeManagerInfo.builder()
                            .changeId(changeManager.getChangeId())
                            .procInsId(procInsId)
                            .infoKey(entry.getKey())
                            .taskId("")
                            .infoValue(Objects.nonNull(value) ? value.toString() : "")
                            .build();
                    infoList.add(info);
                } else if (value instanceof List) {

                    List<Map<String, String>> listMap = (List) value;
                    StringBuilder infoValue = new StringBuilder();
                    for (Map<String, String> v_entry : listMap) {
                        if (v_entry.containsKey("url")) {
                            infoValue.append(v_entry.get("url")).append(";");
                        }
                    }

                    ChangeManagerInfo info = ChangeManagerInfo.builder()
                            .changeId(changeManager.getChangeId())
                            .procInsId(procInsId)
                            .infoKey(entry.getKey())
                            .taskId("")
                            .infoValue(infoValue.toString())
                            .build();
                    infoList.add(info);
                }
            }
        }

        changeManagerInfoService.saveBatch(infoList);
        return flag;
    }

    public List<FlowTaskDto> getChangeFlowRecords(String procInsId, boolean search_all) {
        if (StringUtils.isNotEmpty(procInsId)) {

            Map<String, FlowTaskDto> taskMap = Maps.newLinkedHashMap();

            LambdaQueryWrapper<ChangeManagerSub> subQueryWrapper = new LambdaQueryWrapper<>();
            subQueryWrapper.eq(ChangeManagerSub::getParentProcInsId, procInsId);

            List<ChangeManagerSub> subRecordInfo = changeManagerSubService.list(subQueryWrapper);
            Map<String, String> subTaskInfoMap = subRecordInfo
                    .stream()
                    .collect(Collectors.toMap(ChangeManagerSub::getParentTaskId, ChangeManagerSub::getSubProcInsId));

            // 查询提交的详情
            Map<String, ChangeManagerInfo> changeManagerInfoMap = Maps.newHashMap();
            if (search_all) {
                LambdaQueryWrapper<ChangeManagerInfo> infoQueryWrapper = new LambdaQueryWrapper<>();
                infoQueryWrapper.eq(ChangeManagerInfo::getProcInsId, procInsId);
                List<ChangeManagerInfo> infoList = changeManagerInfoService.list(infoQueryWrapper);
                changeManagerInfoMap = infoList.stream()
                        .filter(p -> StringUtils.isNotEmpty(p.getTaskId()))
                        .collect(Collectors.toMap(ChangeManagerInfo::getTaskId, p -> p));
            }


            List<FlowTaskDto> parentList = flowTaskService.getFlowRecordByInsId(procInsId);

            for (FlowTaskDto taskDto : parentList) {
                String taskName = taskDto.getTaskName();
                String taskId = taskDto.getTaskId();

                if (search_all) {
                    if (changeManagerInfoMap.containsKey(taskId)) {
                        taskDto.setInfoValue(changeManagerInfoMap.get(taskId).getInfoValue());
                    }
                }

                if (subTaskInfoMap.containsKey(taskId)) {
                    //子任务
                    FlowTaskDto parentTaskDto;
                    if (taskMap.containsKey(taskName)) {
                        parentTaskDto = taskMap.get(taskName);
                        parentTaskDto.setFinishTime(taskDto.getFinishTime());
                    } else {
                        //不存在
                        parentTaskDto = new FlowTaskDto();
                        parentTaskDto.setTaskName(taskName);
                        parentTaskDto.setHasChild(true);
                        parentTaskDto.setFinishTime(taskDto.getFinishTime());
                        taskMap.put(taskName, parentTaskDto);
                    }

                    String subProcInsId = subTaskInfoMap.get(taskId);
                    List<FlowTaskDto> subList = flowTaskService.getFlowRecordByInsId(subProcInsId);
                    if (search_all) {
                        for (FlowTaskDto subChild : subList) {
                            String subTaskId = subChild.getTaskId();
                            if (changeManagerInfoMap.containsKey(subTaskId)) {
                                subChild.setInfoValue(changeManagerInfoMap.get(subTaskId).getInfoValue());
                            }
                        }
                    }

                    taskDto.getSubTask().addAll(subList);

                    parentTaskDto.getSubTask().add(taskDto);

                } else {
                    //不是子任务
                    if (taskMap.containsKey(taskName)) {
                        //不是子任务，多实例任务
                        taskMap.get(taskName).getSubTask().add(taskDto);
                    } else {
                        taskMap.put(taskName, taskDto);
                    }
                }
            }

            return Lists.newArrayList(taskMap.values());
        } else {
            return Lists.newArrayList();
        }
    }


    public List<FlowTaskDto> getTodoListByCurrentUser() {
        Long userId = SecurityUtils.getUserId();


        List<FlowTaskDto> todoList = flowTaskService.todoListByUserId(userId.toString());
        for (FlowTaskDto taskDto : todoList) {
            String procInsId = taskDto.getProcInsId();

            LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
            parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);

            ChangeManager parentManager = changeManagerService.getOne(parentQuery);
            if (parentManager == null) {
                //说明是子任务
                LambdaQueryWrapper<ChangeManagerSub> subQuery = new LambdaQueryWrapper<>();
                subQuery.eq(ChangeManagerSub::getSubProcInsId, procInsId);
                ChangeManagerSub subTask = changeManagerSubService.getOne(subQuery);

                String parentProcInsId = subTask.getParentProcInsId();

                parentQuery = new LambdaQueryWrapper<>();
                parentQuery.eq(ChangeManager::getFlowableInstanceId, parentProcInsId);
                parentManager = changeManagerService.getOne(parentQuery);
            }
            taskDto.setProcInsId(parentManager.getFlowableInstanceId());
            taskDto.setChangeId(parentManager.getChangeId().toString());
            taskDto.setChangeTitle(parentManager.getChangeTitle());
            taskDto.setFinishTime(parentManager.getChangeEndTime());
        }

        return todoList;
    }


    @Transactional
    public boolean completeApprove(FlowTaskVo flowTaskVo) {
        if (flowTaskVo.getVariables() == null) {
            flowTaskVo.setVariables(Maps.newHashMap());
        }

        return flowTaskService.complete(flowTaskVo);
    }

    /**
     * 完成提交物 节点
     *
     * @param flowTaskVo
     * @return
     */
    @Transactional
    public boolean completeSubmit(FlowTaskVo flowTaskVo) {
        int subTaskNumber = 0;
        if (flowTaskVo.getCheckSubmits() != null && !flowTaskVo.getCheckSubmits().isEmpty()) {
            subTaskNumber = flowTaskVo.getCheckSubmits().size();
        }

        if (subTaskNumber == 0) {
            return false;
        } else {
            if (flowTaskVo.getChoosePerson() == null || flowTaskVo.getChoosePerson().isEmpty()) {
                throw new PersonException("请正确填写提交人");
            }
            List<Map<String, Object>> subTasks = Lists.newArrayList();
            for (String checkSubmit : flowTaskVo.getCheckSubmits()) {
                if (flowTaskVo.getChoosePerson().containsKey(checkSubmit)) {
                    Map<String, Object> map = (Map<String, Object>) flowTaskVo.getChoosePerson().get(checkSubmit);
                    map.put("title", checkSubmit);
                    subTasks.add(map);
                } else {
                    log.error("{} has no person information", checkSubmit);
                    return false;
                }
            }

            Map<String, Object> taskMap = Maps.newHashMap();

            taskMap.put("sub_number", subTaskNumber);
            taskMap.put("subTasks", subTasks);

            flowTaskVo.setVariables(taskMap);

            return flowTaskService.complete(flowTaskVo);
        }
    }


    @Transactional
    public boolean completeSubmitUpload(FlowTaskVo flowTaskVo) {

        if (flowTaskVo.getVariables() == null) {
            flowTaskVo.setVariables(Maps.newHashMap());
        }

        String procInsId = flowTaskVo.getInstanceId();
        //查询 sub
        LambdaQueryWrapper<ChangeManagerSub> subQuery = new LambdaQueryWrapper<>();
        subQuery.eq(ChangeManagerSub::getSubProcInsId, procInsId);
        ChangeManagerSub subTask = changeManagerSubService.getOne(subQuery);

        // 查询parent
        String parentProcInsId = subTask.getParentProcInsId();

        LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
        parentQuery.eq(ChangeManager::getFlowableInstanceId, parentProcInsId);

        ChangeManager parentManager = changeManagerService.getOne(parentQuery);

        List<ChangeManagerInfo> infoList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : flowTaskVo.getVariables().entrySet()) {
            if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                ChangeManagerInfo info = ChangeManagerInfo.builder()
                        .changeId(parentManager.getChangeId())
                        .procInsId(parentManager.getFlowableInstanceId())
                        .taskId(flowTaskVo.getTaskId())
                        .infoKey(entry.getKey())
                        .infoValue(entry.getValue().toString())
                        .build();

                infoList.add(info);
            }
        }
        changeManagerInfoService.saveBatch(infoList);

        return flowTaskService.complete(flowTaskVo);
    }

    /**
     * 验收报告
     *
     * @param flowTaskVo
     * @return
     */
    @Transactional
    public boolean completeCheckFile(FlowTaskVo flowTaskVo) {
        if (flowTaskVo.getVariables() == null) {
            flowTaskVo.setVariables(Maps.newHashMap());
        }

        String procInsId = flowTaskVo.getInstanceId();

        LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
        parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);

        ChangeManager parentManager = changeManagerService.getOne(parentQuery);

        List<ChangeManagerInfo> infoList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : flowTaskVo.getVariables().entrySet()) {
            if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                ChangeManagerInfo info = ChangeManagerInfo.builder()
                        .changeId(parentManager.getChangeId())
                        .procInsId(parentManager.getFlowableInstanceId())
                        .taskId(flowTaskVo.getTaskId())
                        .infoKey(entry.getKey())
                        .infoValue(entry.getValue().toString())
                        .build();

                infoList.add(info);
            }
        }
        changeManagerInfoService.saveBatch(infoList);

        return flowTaskService.complete(flowTaskVo);
    }


    /**
     * 取消 流程
     *
     * @param flowTaskVo
     */
    public void stopProcess(FlowTaskVo flowTaskVo) {
        flowTaskService.stopProcess(flowTaskVo, FLowStop.reject);
    }

    public void rejectProcess(FlowTaskVo flowTaskVo) {
        flowTaskService.taskReject(flowTaskVo);
    }


    public void assignTask(String sourceUserId, String targetUserId) {
        List<FlowTaskDto> todoList = flowTaskService.todoAllListByUserId(sourceUserId);

        for (FlowTaskDto flowTaskDto : todoList) {

            FlowTaskVo taskVo = new FlowTaskVo();
            taskVo.setTaskId(flowTaskDto.getTaskId());
            taskVo.setAssignee(targetUserId);

            flowTaskService.assignTask(taskVo);
        }
    }
}
