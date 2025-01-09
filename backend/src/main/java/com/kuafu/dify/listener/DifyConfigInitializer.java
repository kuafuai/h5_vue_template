package com.kuafu.dify.listener;

import com.kuafu.dify.config.DifyConfig;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DifyConfigInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private DifyConfig difyConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 延迟执行 initFromDatabase 确保 Flyway 执行完
        difyConfig.initFromDatabase();
    }
}