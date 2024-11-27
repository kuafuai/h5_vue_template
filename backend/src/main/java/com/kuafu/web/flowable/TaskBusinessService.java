package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.ApproveNode;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.service.IApproveNodeService;
import com.kuafu.web.service.IChangeManagerService;
import com.kuafu.web.service.IChangeManagerSubService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("taskBusinessService")
@Slf4j
public class TaskBusinessService {

    @Autowired
    private IApproveNodeService approveNodeService;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private IFlowTaskService flowTaskService;

    @Autowired
    private IChangeManagerService changeManagerService;


    /**
     * 获取 审批变更 人员列表
     *
     * @return
     */
    public List<String> getUsersByApproveNode() {
        log.info("TaskBusinessService=====>查询审批变更用户");
        LambdaQueryWrapper<ApproveNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApproveNode::getApproveNodeName, "审批变更");
        List<ApproveNode> list = approveNodeService.list(queryWrapper);
        if (list != null && !list.isEmpty()) {
            ApproveNode node = list.get(0);
            return Arrays.asList(StringUtils.split(node.getApproveUserId(), ","));
        } else {
            return Lists.newArrayList("1");
        }
    }

    public List<String> getUsersByApproveNodeByEnd() {
        log.info("TaskBusinessService=====>查询最终审核用户");
        LambdaQueryWrapper<ApproveNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApproveNode::getApproveNodeName, "终极审核");
        List<ApproveNode> list = approveNodeService.list(queryWrapper);
        if (list != null && !list.isEmpty()) {
            ApproveNode node = list.get(0);
            return Arrays.asList(StringUtils.split(node.getApproveUserId(), ","));
        } else {
            return Lists.newArrayList("1");
        }
    }


    /**
     * 审批变更 == 完成判断
     *
     * @param execution
     * @return
     */
    public boolean completeApproveNode(DelegateExecution execution) {
        log.info("completeApproveNode===={},{}", execution, execution.getVariables());

        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances");
        Integer nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        return Objects.equals(nrOfCompletedInstances, nrOfInstances);
    }

    /**
     * 上传提交物节点 完成 判断
     *
     * @param execution
     * @return
     */
    public boolean completeSettingSubmitNode(DelegateExecution execution) {
        log.info("completeSettingSubmitNode===={},{}", execution, execution.getVariables());

        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances");
        Integer nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        return Objects.equals(nrOfCompletedInstances, nrOfInstances);
    }

    /**
     * 流程结束
     *
     * @param execution
     */
    public void flowableEnd(DelegateExecution execution) {
        String procInsId = execution.getProcessInstanceId();

        log.info("flowableEnd========{}, {}, {}", procInsId, execution, execution.getVariables());

        LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
        parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);
        ChangeManager parentManager = changeManagerService.getOne(parentQuery);
        if (parentManager != null) {
            Map<String, Object> vars = execution.getVariables();
            if (vars.containsKey("STOP_FLOWABLE_TYPE")) {
                parentManager.setChangeStatus(3);
            } else {
                parentManager.setChangeStatus(2);
            }
            changeManagerService.saveOrUpdate(parentManager);
        }
    }

    /**
     * 提交物 完成判断
     *
     * @param execution
     * @return
     */
    public boolean completeSubmitNode(DelegateExecution execution) {
        log.info("completeSubmitNode======>{},{}", execution, execution.getVariables());

        Integer nrOfInstances = (Integer) execution.getVariable("nrOfInstances");
        Integer nrOfCompletedInstances = (Integer) execution.getVariable("nrOfCompletedInstances");

        if (Objects.equals(nrOfCompletedInstances, nrOfInstances)) {
            String procInsId = execution.getProcessInstanceId();

            LambdaQueryWrapper<ChangeManagerSub> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ChangeManagerSub::getSubProcInsId, procInsId);

            List<ChangeManagerSub> subTasks = changeManagerSubService.list(queryWrapper);

            log.info("completeSubmitNode====={}", subTasks);
            if (subTasks != null && !subTasks.isEmpty()) {
                //完成父级任务
                ChangeManagerSub changeManagerSub = subTasks.get(0);
                String taskId = changeManagerSub.getParentTaskId();
                flowTaskService.complete(taskId);
            }

            return true;
        }

        return false;
    }
}
