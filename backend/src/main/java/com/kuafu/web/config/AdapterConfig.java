package com.kuafu.web.config;

import com.kuafu.web.flowable.SpringSafeAsyncEventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AdapterConfig {

    @Bean
    public SpringSafeAsyncEventBus asyncEventBus() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        return new SpringSafeAsyncEventBus(executor);
    }
}
