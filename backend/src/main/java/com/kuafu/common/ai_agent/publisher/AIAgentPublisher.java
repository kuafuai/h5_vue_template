package com.kuafu.common.ai_agent.publisher;

import com.kuafu.common.ai_agent.entity.AIAgentEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AIAgentPublisher {

    @Resource
    private ApplicationContext applicationContext;




    public void publishEvent(String tableName, String tableId, String primaryName, String mode, String agentFieldName, String prompt, Object data) {
        applicationContext.publishEvent(new AIAgentEvent(this, tableName, tableId, primaryName, mode, agentFieldName, prompt, data));
    }
}
