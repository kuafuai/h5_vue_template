package com.kuafu.common.ai_agent.handler;

import cn.hutool.core.bean.BeanUtil;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafu.common.ai_agent.entity.AIAgentEvent;
import com.kuafu.common.ai_agent.llm.LLmRequest;
import com.kuafu.common.ai_agent.llm.LlmHttpClient;
import com.kuafu.common.ai_agent.llm.LlmRepose;
import com.kuafu.common.util.JSON;
import com.kuafu.common.util.StringUtils;
import com.kuafu.common.util.UUID;
import com.kuafu.web.config.AIAgentConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AgentHandler {



    @Resource
    private ApplicationContext applicationContext;


    @Resource
    private AIAgentConfig aiAgentConfig;

    public void process(AIAgentEvent event) {
        String tableName = event.getTableName();
        String tableId = event.getTableId();
        String primaryName = event.getPrimaryName();
        String agentFieldName = event.getAgentFieldName();
        String prompt = event.getPrompt();
        Object data = event.getData();


        StringBuffer llmPrompt = processLlmPrompt(prompt, data);

//      调用LLM生成结果
        String llmStr = callLlm(llmPrompt.toString());


        updateDataById(event, tableName, agentFieldName, llmStr, primaryName, tableId);
    }

    @NotNull
    private static StringBuffer processLlmPrompt(String prompt, Object data) {
        StringBuffer llmPrompt = new StringBuffer(prompt);
        if (StringUtils.isNotEmpty(prompt) && ObjectUtils.isNotEmpty(data)) {
//           使用
            StringSubstitutor substitutor = new StringSubstitutor(pojoToMap(data));
            String result = substitutor.replace(prompt);
            llmPrompt = new StringBuffer(result);
        }
        return llmPrompt;
    }

    private void updateDataById(AIAgentEvent event, String tableName, String agentFieldName, String llmStr, String primaryName, String tableId) {
        //      更新生成的结果到数据库中
        IService service = (IService) applicationContext.getBean(tableName);
        boolean update = service.update(new UpdateWrapper<>()
                .set(agentFieldName, llmStr).eq(primaryName, tableId));

        if (!update) {
            log.error("update ai agent error {} ", event);
            return;
        }
    }

    private  String callLlm(String llmPrompt) {
        LlmHttpClient llmHttpClient = new LlmHttpClient
                (aiAgentConfig.getBaseUrl());
        LLmRequest request = new LLmRequest(aiAgentConfig.getApiKey());
        request.set("query", llmPrompt);
        HashMap<String, Object> inputs = new HashMap<>();
        inputs.put("prompt", aiAgentConfig.getSystemPrompt());
        request.set("inputs", inputs);
        request.set("user", aiAgentConfig.getUserId());
        String response = llmHttpClient.sendPostRequest(request);

        log.info("llm response {} ", response);


        LlmRepose llmRepose = JSON.parseObject(response, LlmRepose.class);
        String llmStr = "大模型服务异常,请重试";
        if (ObjectUtils.isNotEmpty(llmRepose)) {
            llmStr = llmRepose.getMessage();

            int code = llmRepose.getCode();
            if (code == 0) {
                llmStr = llmRepose.getData().getAnswer();
            }

        }
        return llmStr;
    }


    /**
     * 对象转map
     *
     * @param pojo
     * @return
     */

    public static Map<String, Object> pojoToMap(Object pojo) {

        return BeanUtil.beanToMap(pojo);
    }


}
