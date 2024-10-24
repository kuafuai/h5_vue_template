package com.kuafu.web.flowable;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.ThreadPoolExecutor;

public class SpringSafeAsyncEventBus extends AsyncEventBus {
    private final ThreadPoolExecutor executor;

    public SpringSafeAsyncEventBus(ThreadPoolExecutor executor) {
        super(executor);
        this.executor = executor;
    }
}
