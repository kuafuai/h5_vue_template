package com.kuafu.web.event;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.kuafu.qywx.service.QyWxBusinessService;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.entity.ChangeTakeRecord;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IChangeManagerService;
import com.kuafu.web.service.IChangeManagerSubService;
import com.kuafu.web.service.IChangeTakeRecordService;
import com.kuafu.web.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendQyWxMessageListener implements InitializingBean {

    @Autowired
    private AsyncEventBus asyncEventBus;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private IChangeManagerService changeManagerService;
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private QyWxBusinessService qyWxBusinessService;

    @Autowired
    private IChangeTakeRecordService changeTakeRecordService;

    @Value("${qywx.message.url}")
    private String messageUrl;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("send qywx component register");
        asyncEventBus.register(this);
    }

    @Subscribe
    public void listen(TaskCreateEvent event) {
        log.info("SendQyWxMessageListener========={}", event);
        try {

            String taskName = event.getTaskName();
            String procInsId = event.getProcInsId();
            //分配人
            String assignee = event.getAssignee();

            ChangeManager parentManager = getChangeManager(procInsId);
            if (parentManager == null) {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        log.info("{}", e);
                    }
                    parentManager = getChangeManager(procInsId);
                    if (parentManager != null) {
                        break;
                    }
                }
            }

            Long assigneeUserId = Long.parseLong(assignee);
            UserInfo userInfo = userInfoService.getById(assigneeUserId);
            String userId = userInfo.getUserId();

            String title = "变更任务";
            String description;
            if (parentManager != null) {
                description = "您收到一条：" + parentManager.getChangeProjectName() + "-" + parentManager.getChangeTitle() + "\n"
                        + taskName;
            } else {
                description = "您收到一条：审批变更 任务";
            }

            qyWxBusinessService.sendTextCardMessage(userId, title, description, messageUrl);

            // 插入参与记录
            LambdaQueryWrapper<ChangeTakeRecord> recordQuery = new LambdaQueryWrapper<>();
            recordQuery.eq(ChangeTakeRecord::getUserId, userInfo.getUserInfoId());

            log.info("send wechat msg {},{},{}", procInsId, parentManager, userInfo.getUserInfoId());

            if (parentManager != null) {
                recordQuery.eq(ChangeTakeRecord::getProcInsId, parentManager.getFlowableInstanceId());

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
    }

    private ChangeManager getChangeManager(String procInsId) {

        LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
        parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);
        ChangeManager parentManager = changeManagerService.getOne(parentQuery);
        if (parentManager == null) {
            //说明是子任务
            log.info("未查询到父级变更记录");
            LambdaQueryWrapper<ChangeManagerSub> subQuery = new LambdaQueryWrapper<>();
            subQuery.eq(ChangeManagerSub::getSubProcInsId, procInsId);
            ChangeManagerSub subTask = changeManagerSubService.getOne(subQuery);
            if (subTask != null) {

                String parentProcInsId = subTask.getParentProcInsId();

                parentQuery = new LambdaQueryWrapper<>();
                parentQuery.eq(ChangeManager::getFlowableInstanceId, parentProcInsId);
                parentManager = changeManagerService.getOne(parentQuery);
            } else {
                log.info("未查询到子级变更记录");
            }
        }
        return parentManager;
    }

}
