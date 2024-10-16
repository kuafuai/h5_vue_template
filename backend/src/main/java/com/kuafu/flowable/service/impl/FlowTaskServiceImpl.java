package com.kuafu.flowable.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.util.JSON;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.constant.FlowComment;
import com.kuafu.flowable.constant.ProcessConstants;
import com.kuafu.flowable.domain.*;
import com.kuafu.flowable.factory.FlowServiceFactory;
import com.kuafu.flowable.flow.FindNextNodeUtil;
import com.kuafu.flowable.flow.FlowableUtils;
import com.kuafu.flowable.mapper.FlowFormMapper;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.cmd.AddMultiInstanceExecutionCmd;
import org.flowable.engine.impl.cmd.DeleteMultiInstanceExecutionCmd;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class FlowTaskServiceImpl extends FlowServiceFactory implements IFlowTaskService {

    @Autowired
    private FlowFormMapper flowFormMapper;

    @Autowired
    private IUserInfoService userInfoService;


    @Override
    public boolean complete(FlowTaskVo taskVo) {
        Task task = taskService
                .createTaskQuery()
                .taskId(taskVo.getTaskId())
                .singleResult();

        if (Objects.isNull(task)) {
            return false;
        }

        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(taskVo.getTaskId(), taskVo.getInstanceId(), FlowComment.DELEGATE.getType(), taskVo.getComment());
            taskService.resolveTask(taskVo.getTaskId(), taskVo.getVariables());
        } else {
            taskService.addComment(taskVo.getTaskId(), taskVo.getInstanceId(), FlowComment.NORMAL.getType(), taskVo.getComment());

            Long userId = SecurityUtils.getUserId();
            taskService.setAssignee(taskVo.getTaskId(), userId.toString());
            taskService.complete(taskVo.getTaskId(), taskVo.getVariables());
        }
        return true;
    }

    @Override
    public boolean complete(String taskId) {
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();

        if (Objects.isNull(task)) {
            return false;
        }

        if (DelegationState.PENDING.equals(task.getDelegationState())) {

            taskService.resolveTask(taskId);
        } else {
            taskService.complete(taskId, Maps.newHashMap());
        }
        return true;
    }

    @Override
    public void taskReject(FlowTaskVo flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new BusinessException("任务处于挂起状态!");
        }
        // 当前任务 task
        Task task = taskService
                .createTaskQuery()
                .taskId(flowTaskVo.getTaskId())
                .singleResult();

        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }

        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new BusinessException("当前节点为初始任务节点，不能驳回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list();

        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }


        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService
                .createTaskQuery()
                .processInstanceId(task.getProcessInstanceId()).list();

        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));


        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new BusinessException("任务出现多对多情况，无法撤回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置驳回意见
        currentTaskIds.forEach(item -> taskService.addComment(item, task.getProcessInstanceId(), FlowComment.REJECT.getType(), flowTaskVo.getComment()));

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId()).
                        moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }
        } catch (FlowableObjectNotFoundException e) {
            throw new BusinessException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new BusinessException("无法取消或开始活动");
        }

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void taskReturn(FlowTaskVo flowTaskVo) {

        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new BusinessException("任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService
                .createTaskQuery()
                .taskId(flowTaskVo.getTaskId())
                .singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(flowTaskVo.getTargetKey())) {
                    target = flowElement;
                }
            }
        }

        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, flowTaskVo.getTargetKey(), null, null);
        if (!isSequential) {
            throw new BusinessException("当前节点相对于目标节点，不属于串行关系，无法回退");
        }


        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置回退意见
        currentTaskIds.forEach(currentTaskId -> taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowComment.REBACK.getType(), flowTaskVo.getComment()));

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, flowTaskVo.getTargetKey()).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new BusinessException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new BusinessException("无法取消或开始活动");
        }

    }


    @Override
    public List<UserTask> findReturnTaskList(FlowTaskVo flowTaskVo) {
        // 当前任务 task
        Task task = taskService
                .createTaskQuery()
                .taskId(flowTaskVo.getTaskId())
                .singleResult();

        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId())
                .singleResult();

        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        return userTaskList;
    }

    @Override
    public void deleteTask(FlowTaskVo flowTaskVo) {
        taskService.deleteTask(flowTaskVo.getTaskId(), flowTaskVo.getComment());
    }

    /**
     * 认领/签收任务
     * 认领以后,这个用户就会成为任务的执行人,任务会从其他成员的任务列表中消失
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(FlowTaskVo flowTaskVo) {
        String userId = flowTaskVo.getUserId();
        if (StringUtils.isEmpty(userId)) {
            userId = String.valueOf(SecurityUtils.getUserId());
        }
        taskService.claim(flowTaskVo.getTaskId(), userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unClaim(FlowTaskVo flowTaskVo) {
        taskService.unclaim(flowTaskVo.getTaskId());
    }

    /**
     * 委派任务
     * 任务委派只是委派人将当前的任务交给被委派人进行审批，处理任务后又重新回到委派人身上。
     *
     * @param flowTaskVo 请求实体参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(FlowTaskVo flowTaskVo) {
        taskService.delegateTask(flowTaskVo.getTaskId(), flowTaskVo.getAssignee());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolveTask(FlowTaskVo flowTaskVo) {
        taskService.resolveTask(flowTaskVo.getTaskId());
    }

    @Override
    public void assignTask(FlowTaskVo flowTaskVo) {
        // 直接转派就可以覆盖掉之前的
        taskService.setAssignee(flowTaskVo.getTaskId(), flowTaskVo.getAssignee());
    }

    /**
     * 多实例加签
     * act_ru_task、act_ru_identitylink各生成一条记录
     *
     * @param flowTaskVo
     */
    @Override
    public void addMultiInstanceExecution(FlowTaskVo flowTaskVo) {
        managementService.executeCommand(new AddMultiInstanceExecutionCmd(flowTaskVo.getDefId(), flowTaskVo.getInstanceId(), flowTaskVo.getVariables()));
    }

    @Override
    public void deleteMultiInstanceExecution(FlowTaskVo flowTaskVo) {
        managementService.executeCommand(new DeleteMultiInstanceExecutionCmd(flowTaskVo.getCurrentChildExecutionId(), flowTaskVo.getFlag()));
    }


    @Override
    public Page<FlowTaskDto> myProcess(FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page<>();
        Long userId = SecurityUtils.getUserId();
        //根据发起人，查询启动的实例
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId.toString())
                .orderByProcessInstanceStartTime()
                .desc();

        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
                .listPage(queryVo.getPageSize() * (queryVo.getCurrent() - 1), queryVo.getPageSize());

        page.setTotal(historicProcessInstanceQuery.count());

        List<FlowTaskDto> flowList = new ArrayList<>();

        for (HistoricProcessInstance hisIns : historicProcessInstances) {
            FlowTaskDto flowTask = new FlowTaskDto();
            flowTask.setCreateTime(hisIns.getStartTime());
            flowTask.setFinishTime(hisIns.getEndTime());
            flowTask.setProcInsId(hisIns.getId());

            // 计算耗时
            if (Objects.nonNull(hisIns.getEndTime())) {
                long time = hisIns.getEndTime().getTime() - hisIns.getStartTime().getTime();
                flowTask.setDuration(getDate(time));
            } else {
                long time = System.currentTimeMillis() - hisIns.getStartTime().getTime();
                flowTask.setDuration(getDate(time));
            }


            // 流程定义信息
            ProcessDefinition pd = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionId(hisIns.getProcessDefinitionId())
                    .singleResult();

            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setCategory(pd.getCategory());
            flowTask.setProcDefVersion(pd.getVersion());

            // 当前所处流程
            List<Task> taskList = taskService
                    .createTaskQuery()
                    .processInstanceId(hisIns.getId())
                    .list();

            if (CollectionUtils.isNotEmpty(taskList)) {
                //当前有未完成
                flowTask.setTaskId(taskList.get(0).getId());
                flowTask.setTaskName(taskList.get(0).getName());
                if (org.apache.commons.lang3.StringUtils.isNotBlank(taskList.get(0).getAssignee())) {
                    // 当前任务节点办理人信息
                    Long assigneeId = Long.parseLong(taskList.get(0).getAssignee());
                    UserInfo userInfo = userInfoService.getById(assigneeId);
                    if (Objects.nonNull(userInfo)) {
                        flowTask.setAssigneeId(userInfo.getUserInfoId());
                        flowTask.setAssigneeName(userInfo.getUserName());
                    }
                }
            } else {
                List<HistoricTaskInstance> historicTaskInstance = historyService
                        .createHistoricTaskInstanceQuery()
                        .processInstanceId(hisIns.getId())
                        .orderByHistoricTaskInstanceEndTime()
                        .desc().
                        list();

                flowTask.setTaskId(historicTaskInstance.get(0).getId());
                flowTask.setTaskName(historicTaskInstance.get(0).getName());

                if (org.apache.commons.lang3.StringUtils.isNotBlank(historicTaskInstance.get(0).getAssignee())) {
                    // 当前任务节点办理人信息
                    Long assigneeId = Long.parseLong(historicTaskInstance.get(0).getAssignee());
                    UserInfo userInfo = userInfoService.getById(assigneeId);
                    if (Objects.nonNull(userInfo)) {
                        flowTask.setAssigneeId(userInfo.getUserInfoId());
                        flowTask.setAssigneeName(userInfo.getUserName());
                    }
                }
            }
            flowList.add(flowTask);

        }
        page.setRecords(flowList);
        return page;
    }

    @Override
    public void stopProcess(FlowTaskVo flowTaskVo) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(flowTaskVo.getInstanceId()).list();
        if (CollectionUtils.isEmpty(task)) {
            throw new BusinessException("流程未启动或已执行完成，取消申请失败");
        }
        // 获取当前流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(flowTaskVo.getInstanceId())
                .singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (Objects.nonNull(bpmnModel)) {
            Process process = bpmnModel.getMainProcess();
            List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class, false);
            if (CollectionUtils.isNotEmpty(endNodes)) {

                // 获取当前流程最后一个节点
                String endId = endNodes.get(0).getId();
                List<Execution> executions = runtimeService.createExecutionQuery()
                        .parentId(processInstance.getProcessInstanceId()).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                // 变更流程为已结束状态
                runtimeService.createChangeActivityStateBuilder()
                        .moveExecutionsToSingleActivityId(executionIds, endId).changeState();
            }
        }
    }

    @Override
    public void revokeProcess(FlowTaskVo flowTaskVo) {

    }

    @Override
    public Page<FlowTaskDto> todoList(FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page<>();

        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                //.taskCandidateGroupIn(sysUser.getRoles().stream().map(role -> role.getRoleId().toString()).collect(Collectors.toList()))
                .taskCandidateOrAssigned(String.valueOf(SecurityUtils.getUserId()))
                .orderByTaskCreateTime()
                .desc();

        if (org.apache.commons.lang3.StringUtils.isNotBlank(queryVo.getName())) {
            taskQuery.processDefinitionNameLike(queryVo.getName());
        }

        page.setTotal(taskQuery.count());
        List<Task> taskList = taskQuery.listPage(queryVo.getPageSize() * (queryVo.getCurrent() - 1), queryVo.getPageSize());
        List<FlowTaskDto> flowList = new ArrayList<>();

        for (Task task : taskList) {
            FlowTaskDto flowTask = new FlowTaskDto();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());

            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setExecutionId(task.getExecutionId());
            flowTask.setTaskName(task.getName());

            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();

            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            Long startUserId = Long.parseLong(historicProcessInstance.getStartUserId());
            UserInfo userInfo = userInfoService.getById(startUserId);
            if (Objects.nonNull(userInfo)) {
                flowTask.setStartUserId(historicProcessInstance.getStartUserId());
                flowTask.setStartUserName(userInfo.getUserName());
            }
            flowList.add(flowTask);
        }

        page.setRecords(flowList);

        return page;
    }

    /**
     * 获取最近10条的代办任务
     *
     * @param userId
     * @return
     */
    @Override
    public List<FlowTaskDto> todoListByUserId(String userId) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                //.taskCandidateGroupIn(sysUser.getRoles().stream().map(role -> role.getRoleId().toString()).collect(Collectors.toList()))
                .taskCandidateOrAssigned(String.valueOf(SecurityUtils.getUserId()))
                .orderByTaskCreateTime()
                .desc();

        List<Task> taskList = taskQuery.listPage(0, 10);
        List<FlowTaskDto> flowList = new ArrayList<>();

        for (Task task : taskList) {
            FlowTaskDto flowTask = new FlowTaskDto();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());

            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setExecutionId(task.getExecutionId());
            flowTask.setTaskName(task.getName());

            long time = System.currentTimeMillis() - task.getCreateTime().getTime();
            flowTask.setDuration(getDate(time));

            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();

            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            Long startUserId = Long.parseLong(historicProcessInstance.getStartUserId());
            UserInfo userInfo = userInfoService.getById(startUserId);
            if (Objects.nonNull(userInfo)) {
                flowTask.setStartUserId(historicProcessInstance.getStartUserId());
                flowTask.setStartUserName(userInfo.getUserName());
            }
            flowList.add(flowTask);
        }

        return flowList;
    }

    @Override
    public Page<FlowTaskDto> finishedList(FlowQueryVo queryVo) {
        Page<FlowTaskDto> page = new Page<>();

        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskAssignee(SecurityUtils.getUserId().toString())
                .orderByHistoricTaskInstanceEndTime()
                .desc();

        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery
                .listPage(queryVo.getPageSize() * (queryVo.getCurrent() - 1), queryVo.getPageSize());

        List<FlowTaskDto> hisTaskList = new ArrayList<>();

        for (HistoricTaskInstance histTask : historicTaskInstanceList) {
            FlowTaskDto flowTask = new FlowTaskDto();
            // 当前流程信息
            flowTask.setTaskId(histTask.getId());
            // 审批人员信息
            flowTask.setCreateTime(histTask.getCreateTime());
            flowTask.setFinishTime(histTask.getEndTime());

            flowTask.setDuration(getDate(histTask.getDurationInMillis()));
            flowTask.setProcDefId(histTask.getProcessDefinitionId());
            flowTask.setTaskDefKey(histTask.getTaskDefinitionKey());
            flowTask.setTaskName(histTask.getName());

            // 流程定义信息
            ProcessDefinition pd = repositoryService
                    .createProcessDefinitionQuery()
                    .processDefinitionId(histTask.getProcessDefinitionId())
                    .singleResult();

            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(histTask.getProcessInstanceId());
            flowTask.setHisProcInsId(histTask.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(histTask.getProcessInstanceId())
                    .singleResult();
            Long startUserId = Long.parseLong(historicProcessInstance.getStartUserId());
            UserInfo userInfo = userInfoService.getById(startUserId);
            if (Objects.nonNull(userInfo)) {
                flowTask.setStartUserId(historicProcessInstance.getStartUserId());
                flowTask.setStartUserName(userInfo.getUserName());
            }
            hisTaskList.add(flowTask);
        }

        page.setTotal(taskInstanceQuery.count());
        page.setRecords(hisTaskList);

        return page;
    }


    @Override
    public Map<String, Object> flowRecord(String procInsId, String deployId) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (org.apache.commons.lang3.StringUtils.isNotBlank(procInsId)) {
            List<HistoricActivityInstance> list = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(procInsId)
                    .orderByHistoricActivityInstanceStartTime()
                    .desc()
                    .list();

            List<FlowTaskDto> hisFlowList = new ArrayList<>();

            for (HistoricActivityInstance histIns : list) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(histIns.getTaskId())) {

                    FlowTaskDto flowTask = new FlowTaskDto();

                    flowTask.setTaskId(histIns.getTaskId());
                    flowTask.setTaskName(histIns.getActivityName());
                    flowTask.setCreateTime(histIns.getStartTime());
                    flowTask.setFinishTime(histIns.getEndTime());

                    if (org.apache.commons.lang3.StringUtils.isNotBlank(histIns.getAssignee())) {
                        Long assigneeId = Long.parseLong(histIns.getAssignee());
                        UserInfo userInfo = userInfoService.getById(assigneeId);
                        if (Objects.nonNull(userInfo)) {
                            flowTask.setAssigneeId(Long.parseLong(histIns.getAssignee()));
                            flowTask.setAssigneeName(userInfo.getUserName());
                        }
                    }

                    // 展示审批人员
                    List<HistoricIdentityLink> linksForTask = historyService
                            .getHistoricIdentityLinksForTask(histIns.getTaskId());
                    StringBuilder stringBuilder = new StringBuilder();
                    for (HistoricIdentityLink identityLink : linksForTask) {
                        // 获选人,候选组/角色(多个)
                        if ("candidate".equals(identityLink.getType())) {
                            if (org.apache.commons.lang3.StringUtils.isNotBlank(identityLink.getUserId())) {
                                // SysUser sysUser = sysUserService.selectUserById(Long.parseLong(identityLink.getUserId()));
                                // stringBuilder.append(sysUser.getNickName()).append(",");
                            }
                            if (org.apache.commons.lang3.StringUtils.isNotBlank(identityLink.getGroupId())) {
                                // SysRole sysRole = sysRoleService.selectRoleById(Long.parseLong(identityLink.getGroupId()));
                                // stringBuilder.append(sysRole.getRoleName()).append(",");
                            }
                        }
                    }
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(stringBuilder)) {
                        flowTask.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                    }


                    flowTask.setDuration(histIns.getDurationInMillis() == null || histIns.getDurationInMillis() == 0 ? null : getDate(histIns.getDurationInMillis()));
                    // 获取意见评论内容
                    List<Comment> commentList = taskService.getProcessInstanceComments(histIns.getProcessInstanceId());
                    commentList.forEach(comment -> {
                        if (histIns.getTaskId().equals(comment.getTaskId())) {
                            flowTask.setComment(FlowCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                        }
                    });
                    hisFlowList.add(flowTask);

                }
            }
            map.put("flowList", hisFlowList);
        }
        // 第一次申请获取初始化表单
        if (org.apache.commons.lang3.StringUtils.isNotBlank(deployId)) {
            FlowFormDto formDto = flowFormMapper.selectFormByDeployId(deployId);
            if (Objects.nonNull(formDto)) {
                map.put("formData", JSON.parseObject(formDto.getFormContent(), Map.class));
            }
        }
        return map;
    }


    @Override
    public List<FlowTaskDto> getFlowRecordByInsId(String procInsId) {
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        List<FlowTaskDto> hisFlowList = new ArrayList<>();

        for (HistoricActivityInstance histIns : list) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(histIns.getTaskId())) {

                FlowTaskDto flowTask = new FlowTaskDto();

                flowTask.setTaskId(histIns.getTaskId());
                flowTask.setTaskName(histIns.getActivityName());
                flowTask.setCreateTime(histIns.getStartTime());
                flowTask.setFinishTime(histIns.getEndTime());
                flowTask.setProcInsId(histIns.getProcessInstanceId());

                if (org.apache.commons.lang3.StringUtils.isNotBlank(histIns.getAssignee())) {
                    Long assigneeId = Long.parseLong(histIns.getAssignee());
                    UserInfo userInfo = userInfoService.getById(assigneeId);
                    if (Objects.nonNull(userInfo)) {
                        flowTask.setAssigneeId(Long.parseLong(histIns.getAssignee()));
                        flowTask.setAssigneeName(userInfo.getUserName());
                    }
                }

                //处理一下任务名为变量
                if (StringUtils.startsWith(flowTask.getTaskName(), "$")) {

                    Map<String, Object> varMaps = processVariables(flowTask.getTaskId());

                    String expression = flowTask.getTaskName();
                    expression = expression.substring(expression.lastIndexOf("{") + 1, expression.lastIndexOf("}"));
                    Expression exp = AviatorEvaluator.compile(expression);

                    String newTaskName = exp.execute(varMaps).toString();

                    flowTask.setTaskName(newTaskName);
                }

                flowTask.setDuration(histIns.getDurationInMillis() == null || histIns.getDurationInMillis() == 0 ? null : getDate(histIns.getDurationInMillis()));
                // 获取意见评论内容
                List<Comment> commentList = taskService.getProcessInstanceComments(histIns.getProcessInstanceId());
                commentList.forEach(comment -> {
                    if (histIns.getTaskId().equals(comment.getTaskId())) {
                        flowTask.setComment(FlowCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                    }
                });
                hisFlowList.add(flowTask);

            }
        }

        return hisFlowList;
    }

    @Override
    public String getTaskForm(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return task.getFormKey();
    }

    @Override
    public List<FlowViewerDto> getFlowViewer(String procInsId, String executionId) {
        List<FlowViewerDto> flowViewerList = new ArrayList<>();

        // 获取任务开始节点(临时处理方式)
        List<HistoricActivityInstance> startNodeList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .listPage(0, 3);

        for (HistoricActivityInstance startInstance : startNodeList) {

            if (!"sequenceFlow".equals(startInstance.getActivityType())) {

                FlowViewerDto flowViewerDto = new FlowViewerDto();

                flowViewerDto.setKey(startInstance.getActivityId());
                // 根据流程节点处理时间校验改节点是否已完成
                flowViewerDto.setCompleted(!Objects.isNull(startInstance.getEndTime()));
                flowViewerList.add(flowViewerDto);
            }
        }

        // 历史节点
        List<HistoricActivityInstance> hisActIns = historyService
                .createHistoricActivityInstanceQuery()
                .executionId(executionId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        for (HistoricActivityInstance activityInstance : hisActIns) {
            if (!"sequenceFlow".equals(activityInstance.getActivityType())) {
                FlowViewerDto flowViewerDto = new FlowViewerDto();
                flowViewerDto.setKey(activityInstance.getActivityId());
                // 根据流程节点处理时间校验改节点是否已完成
                flowViewerDto.setCompleted(!Objects.isNull(activityInstance.getEndTime()));
                flowViewerList.add(flowViewerDto);
            }
        }

        return flowViewerList;

    }


    @Override
    public Map<String, Object> processVariables(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService
                .createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskId(taskId)
                .singleResult();
        if (Objects.nonNull(historicTaskInstance)) {
            return historicTaskInstance.getProcessVariables();
        } else {
            return taskService.getVariables(taskId);
        }
    }


    @Override
    public FlowNextDto getNextFlowNode(FlowTaskVo flowTaskVo) {
        // Step 1. 获取当前节点并找到下一步节点
        Task task = taskService.createTaskQuery()
                .taskId(flowTaskVo.getTaskId())
                .singleResult();
        if (Objects.isNull(task)) {
            return null;
        }

        // Step 2. 获取当前流程所有流程变量(网关节点时需要校验表达式)
        Map<String, Object> variables = taskService.getVariables(task.getId());
        List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasks(repositoryService, task, variables);
        if (CollectionUtils.isEmpty(nextUserTask)) {
            return null;
        }
        return getFlowAttribute(nextUserTask);
    }

    @Override
    public FlowNextDto getNextFlowNodeByStart(FlowTaskVo flowTaskVo) {

        // Step 1. 查找流程定义信息
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(flowTaskVo.getDeploymentId())
                .singleResult();

        if (Objects.isNull(processDefinition)) {
            return null;
        }
        // Step 2. 获取下一任务节点(网关节点时需要校验表达式)
        List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasksByStart(repositoryService,
                processDefinition, flowTaskVo.getVariables());
        if (CollectionUtils.isEmpty(nextUserTask)) {
            return null;
        }
        return getFlowAttribute(nextUserTask);
    }


    /**
     * 获取任务节点属性,包含自定义属性等
     *
     * @param nextUserTask
     */
    private FlowNextDto getFlowAttribute(List<UserTask> nextUserTask) {
        FlowNextDto flowNextDto = new FlowNextDto();
        for (UserTask userTask : nextUserTask) {
            MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();

            // 会签节点
            if (Objects.nonNull(multiInstance)) {
                flowNextDto.setVars(multiInstance.getInputDataItem());
                flowNextDto.setType(ProcessConstants.PROCESS_MULTI_INSTANCE);
                flowNextDto.setDataType(ProcessConstants.DYNAMIC);
            } else {
                // 读取自定义节点属性 判断是否是否需要动态指定任务接收人员、组
                String dataType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_DATA_TYPE);
                String userType = userTask.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_CUSTOM_USER_TYPE);
                flowNextDto.setVars(ProcessConstants.PROCESS_APPROVAL);
                flowNextDto.setType(userType);
                flowNextDto.setDataType(dataType);
            }
        }
        return flowNextDto;
    }


    @Override
    public FlowFormDto flowFormData(String deployId) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(deployId)) {
            return flowFormMapper.selectFormByDeployId(deployId);
        }
        return null;
    }

    @Override
    public void flowXmlAndNode(String procInsId, String deployId) {

    }

    @Override
    public Map<String, Object> flowTaskForm(String taskId) throws Exception {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 流程变量
        Map<String, Object> parameters = new HashMap<>();
        HistoricTaskInstance historicTaskInstance = historyService
                .createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskId(taskId)
                .singleResult();

        if (Objects.nonNull(historicTaskInstance)) {
            parameters = historicTaskInstance.getProcessVariables();
        } else {
            parameters = taskService.getVariables(taskId);
        }

        Map<String, Object> oldVariables = JSON.parseObject(JSON.toJSONString(parameters.get("formJson")), Map.class);

        List<Map<String, Object>> oldFields = JSON.parseArray(JSON.toJSONString(oldVariables.get("widgetList")));

        // 设置已填写的表单为禁用状态
        for (Map<String, Object> oldField : oldFields) {
            Map<String, Object> options = (Map<String, Object>) oldField.get("options");
            options.put("disabled", true);
        }

        if (org.apache.commons.lang3.StringUtils.isNotBlank(task.getFormKey())) {
            FlowFormDto flowFormDto = flowFormMapper.selectFormById(Integer.valueOf(task.getFormKey()));
            Map<String, Object> data = JSON.parseObject(flowFormDto.getFormContent(), Map.class);

            List<Map<String, Object>> newFields = JSON.parseArray(JSON.toJSONString(data.get("widgetList")));

            // 表单回显时 加入子表单信息到流程变量中
            for (Map<String, Object> newField : newFields) {
                String key = String.valueOf(newField.get("id"));
                // 处理图片上传组件回显问题
                if ("picture-upload".equals(newField.get("type").toString())) {
                    parameters.put(key, new ArrayList<>());
                } else {
                    parameters.put(key, null);
                }
            }
            oldFields.addAll(newFields);
        }

        oldVariables.put("widgetList", oldFields);
        parameters.put("formJson", oldVariables);

        return parameters;
    }

    @Override
    public void flowTaskInfo(String procInsId, String elementId) {

    }

    /**
     * 流程完成时间处理
     *
     * @param ms
     * @return
     */
    private String getDate(long ms) {

        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }
}
