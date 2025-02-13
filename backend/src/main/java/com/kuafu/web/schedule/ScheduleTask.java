package com.kuafu.web.schedule;

import com.kuafu.qywx.service.QyWxBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    private QyWxBusinessService qyWxBusinessService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void syncUser() {
        log.info("=========同步企业微信用户==================");
        qyWxBusinessService.syncUser();
    }
}
