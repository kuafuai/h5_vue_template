package com.kuafu.dify.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kuafu.dify.config.DifyConfig;
import com.kuafu.dify.constant.DifyTenantConstant;
import com.kuafu.dify.entity.DifyFileVO;
import com.kuafu.dify.request.DifyRequest;
import com.kuafu.dify.response.DifyResponse;
import com.kuafu.dify.service.DifyTenantService;
import com.kuafu.llm.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.kuafu.dify.constant.DifyConstant.*;

/**
 * 租户类Service
 */
@Service
@Slf4j
public class DifyTenantServiceImpl implements DifyTenantService {
    @Resource
    private DifyConfig difyConfig;
    private Gson gson = new Gson();
    @Override
    public void callApiStream(DifyRequest difyRequest, String conversationId, String userId, SseEmitter sseEmitter) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.MINUTES)
                .build();
        // Create the request body as a Map
        Map<String, Object> params = new HashMap<>();
        params.put(INPUTS, difyRequest.getInputs());
        params.put(QUERY, difyRequest.getQuery());
        params.put(CONVERSATION_ID, conversationId);
        params.put(USER, userId);
        params.put(RESPONSE_MODE, RESPONSE_MODE_STREAMING);


        // Convert the Map to a JSON string
        String jsonString = gson.toJson(params);

        // Create the request body
        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                jsonString
        );

        // Build the request
        Request request = new Request.Builder()
                .url(difyConfig.getBaseUrl() + DifyTenantConstant.CHAT_STREAM_URL)
                .post(body)
                .addHeader("Authorization", difyConfig.getApiKey()) // Replace with actual method to get the API key
                .addHeader("Content-Type", "application/json")
                .build();

        EventSourceListener listener = new EventSourceListener() {
            String conversationId;

            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                log.info("Connection opened");
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data) {

                JsonObject jsonObject = gson.fromJson(data, JsonObject.class);

                System.out.println(jsonObject);

                String event = jsonObject.get("event").getAsString();

                conversationId = jsonObject.get("conversation_id").getAsString();

                HashMap<String, String> messageMap = new HashMap<>();
                messageMap.put("event", event);
                messageMap.put("conversation_id", conversationId);

                if ("message".equals(event)) {

                    String answer = jsonObject.get("answer").getAsString();

                    messageMap.put("answer", answer);
                }

                try {
                    sseEmitter.send(SseEmitter.event().name("message").data(gson.toJson(messageMap)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                log.info("Connection closed,conversationId is{} ", conversationId);
                sseEmitter.complete();
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                try {
                    log.info("error: {}", response.body().string());
                    sseEmitter.complete();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        EventSource.Factory factory = EventSources.createFactory(client);
        EventSource eventSource = factory.newEventSource(request, listener);

    }


    @Override
    public DifyResponse callApiBlock(DifyRequest difyRequest, String conversationId, String userId) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();

        // Create the request body as a Map
        Map<String, Object> jsonPayload = new HashMap<>();
        jsonPayload.put("inputs", new HashMap<>()); // Empty map for "inputs"
        jsonPayload.put("query", difyRequest.getQuery());
        jsonPayload.put("response_mode", "blocking");
        jsonPayload.put("conversation_id", conversationId);
        jsonPayload.put("user", userId);

        // Convert the Map to a JSON string
        String jsonString = gson.toJson(jsonPayload);

        // Create the request body
        RequestBody body = RequestBody.create(
                MediaType.get("application/json; charset=utf-8"),
                jsonString
        );

        // Build the request
        Request request = new Request.Builder()
                .url(difyConfig.getBaseUrl() + DifyTenantConstant.CHAT_BLOCK_URL)
                .post(body)
                .addHeader("Authorization", difyConfig.getApiKey()) // Replace with actual method to get the API key
                .addHeader("Content-Type", "application/json")
                .build();

        Response execute = null;
        try {
            execute = client.newCall(request).execute();
            String string = execute.body().string();


            JsonObject jsonObject = gson.fromJson(string, JsonObject.class);

            String event = jsonObject.get("event").getAsString();
            if (event.equals("message")) {
                String conversation_id = jsonObject.get("conversation_id").getAsString();
                String answer = jsonObject.get("answer").getAsString();
                DifyResponse difyResponse = new DifyResponse();
                difyResponse.setAnswer(answer);
                difyResponse.setConversationId(conversation_id);
                return difyResponse;
            } else {
                System.out.println(jsonObject);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DifyFileVO callUpload(MultipartFile multipartFile, DifyConfig difyConfig) {
        return null;
    }
}
