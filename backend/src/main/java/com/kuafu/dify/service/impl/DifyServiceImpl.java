package com.kuafu.dify.service.impl;

import cn.hutool.core.text.UnicodeUtil;
import com.alibaba.dashscope.utils.JsonUtils;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.dify.config.DifyConfig;
import com.kuafu.dify.entity.DifyFileVO;
import com.kuafu.dify.request.DifyRequest;
import com.kuafu.dify.service.DifyService;
import com.kuafu.llm.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.kuafu.dify.constant.DifyConstant.*;


@Component
@Slf4j
public class DifyServiceImpl implements DifyService {
    @Resource
    private DifyConfig difyConfig;

    private final Gson gson = new Gson();

    /**
     * 流式调用
     *
     * @param difyRequest
     * @param conversationId
     * @param userId
     * @param sseEmitter
     */
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
                .url(difyConfig.getBaseUrl() + CHAT_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + difyConfig.getApiKey()) // Replace with actual method to get the API key
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

    /**
     * 阻塞式调用
     *
     * @param difyRequest
     * @param conversationId
     * @param userId
     * @return
     */
    @Override
    public ChatResponse callApiBlock(DifyRequest difyRequest, String conversationId, String userId) {

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
                .url(difyConfig.getApiUrl() + "/chat-messages")
                .post(body)
                .addHeader("Authorization", "Bearer " + difyConfig.getApiKey()) // Replace with actual method to get the API key
                .addHeader("Content-Type", "application/json")
                .build();

        Response execute = null;
        try {
            execute = client.newCall(request).execute();
            String string = execute.body().string();

            log.info("======{}", string);

            JsonObject jsonObject = gson.fromJson(string, JsonObject.class);

            String event = jsonObject.get("event").getAsString();
            if (event.equals("message")) {
                String conversation_id = jsonObject.get("conversation_id").getAsString();
                String answer = jsonObject.get("answer").getAsString();
                ChatResponse chatResponse = new ChatResponse();
                chatResponse.setAnswer(answer);
                chatResponse.setConversionId(conversation_id);
                return chatResponse;
            } else {
                System.out.println(jsonObject);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
/**
 * 调用 dify 图片上传
 *
 * @param multipartFile
 * @return
 * @throws IOException
 */
    public DifyFileVO callUpload(MultipartFile multipartFile, DifyConfig difyConfig) {

        DifyFileVO difyFileVO;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();

            // 构建 MultipartBody，用于上传文件
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                            "file", // 字段名，根据后端接口需求调整
                            multipartFile.getOriginalFilename(), // 文件名
                            RequestBody.create(
                                    MediaType.parse(Objects.requireNonNull(multipartFile.getContentType())), // 媒体类型
                                    multipartFile.getBytes() // 文件内容
                            )
                    );
            Request request = new Request.Builder()
                    .url(difyConfig.getUploadUrl())
                    .post(builder.build())
                    .addHeader("Authorization", "Bearer " + difyConfig.getApiKey())
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            // 获取响应体
            if (!response.isSuccessful()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "Failed to get the content");
            }

            String responseBody = response.body().string();

            // 转换 Unicode 转义为中文

            String decode = UnicodeUtil.toString(responseBody);
            difyFileVO = JsonUtils.fromJson(decode, DifyFileVO.class);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
        return difyFileVO;

    }


    @Override
    public XWPFTemplate generateWordXWPFTemplate(String answer) {
        Map<String, Object> content = new HashMap<>();
        content.put("title", "Java 全栈知识体系");
        content.put("author", "艾伦");
        content.put("content", answer);
        content.put("site", new HyperlinkTextRenderData("", ""));

        content.put("poiText", "Apache POI 是创建和维护操作各种符合Office Open XML（OOXML）标准和微软的OLE 2复合文档格式（OLE2）的Java API。用它可以使用Java读取和创建,修改MS Excel文件.而且,还可以使用Java读取和创建MS Word和MSPowerPoint文件。更多请参考[官方文档](https://poi.apache.org/index.html)");

        content.put("poiText2", "生成xls和xlsx有什么区别？POI对Excel中的对象的封装对应关系？");
        content.put("poiList", Numberings.create("excel03只能打开xls格式，无法直接打开xlsx格式",
                "xls只有65536行、256列; xlsx可以有1048576行、16384列",
                "xls占用空间大, xlsx占用空间小，运算速度也会快一点"));

        RowRenderData headRow = Rows.of("序号", "姓名", "邮箱", "手机号", "描述").textColor("FFFFFF")
                .bgColor("4472C4").center().create();


        XWPFTemplate render = null;
        try {
            render = XWPFTemplate.compile(new ClassPathResource("docx_template.docx").getFile()).render(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return render;
    }
}
