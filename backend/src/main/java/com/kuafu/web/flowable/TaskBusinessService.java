package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.ApproveNode;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.service.IApproveNodeService;
import com.kuafu.web.service.IChangeManagerSubService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("taskBusinessService")
@Slf4j
public class TaskBusinessService {

    @Autowired
    private IApproveNodeService approveNodeService;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private IFlowTaskService flowTaskService;


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
            //todo 配置默认用户
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
        log.info("===={},{}", execution, execution.getVariables());
        return true;
    }


    /**
     * 提交物 完成判断
     *
     * @param execution
     * @return
     */
    public boolean completeSubmitNode(DelegateExecution execution) {
        log.info("completeSubmitNode======>{},{}", execution, execution.getVariables());
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
}
