package com.kuafu.dify.controller;

import com.deepoove.poi.XWPFTemplate;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.dify.config.DifyConfig;
import com.kuafu.dify.entity.DownLoadFileRequest;
import com.kuafu.dify.manager.DifyManager;
import com.kuafu.dify.request.ChatbotRequest;
import com.kuafu.dify.request.DifyRequest;
import com.kuafu.dify.response.DifyResponse;
import com.kuafu.dify.service.DifyService;
import com.kuafu.llm.config.PromptConfig;
import com.kuafu.llm.model.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;



@Slf4j
@RestController
@RequestMapping("chatbot")
public class ChatbotController {

    @Resource
    private DifyService difyService;

    @Resource
    private DifyManager difyManager;

    @Resource
    private DifyConfig difyConfig;



    /**
     * 对话流
     *
     * @param
     * @return
     */
    @PostMapping(value = "")
    public SseEmitter stream(@RequestBody ChatbotRequest chatbotRequest, HttpServletRequest request) {

        // 用于创建一个 SSE 连接对象
        SseEmitter emitter = new SseEmitter(3600000L);
        String conversationId = chatbotRequest.getConversationId();
        String query = chatbotRequest.getQuery();
        String userId = chatbotRequest.getUserId();
        if (StringUtils.isEmpty(conversationId)) {
            query = PromptConfig.PROMPT + "\n" + query;
        }

        String docContent = null;

        String url = chatbotRequest.getFileUrl();
        if(StringUtils.isNotBlank(url)) {
            String filePath = difyManager.getFilePathByUrl(url);
            docContent = difyManager.getFileContentByFilePath(filePath);

        }

        query = docContent + "\n" + query;

        log.info("content : {}", query);
        // todo 从数据库中查询
//        chatbotRequest.setPrompt("你是青青草原懒羊羊，也称懒大王");

        DifyRequest difyRequest = difyManager.getDifyRequest(chatbotRequest);

        if("dify".equals(difyConfig.getLlmType())) {
            difyManager.callDifyStream(difyRequest, conversationId, userId, emitter);
        }
        if("codeflying".equals(difyConfig.getLlmType())) {
            difyManager.callDifyTenantStream(difyRequest, conversationId, userId, emitter);
        }

        // 在后台线程中模拟实时数据
        return emitter;
    }

    /**
     * 对话流
     *
     * @param
     * @return
     */
    @PostMapping(value = "/chat_block")
    public ChatResponse block(@RequestBody ChatbotRequest chatbotRequest, HttpServletRequest request) {

        String conversationId = chatbotRequest.getConversationId();
        String query = chatbotRequest.getQuery();
        String userId = chatbotRequest.getUserId();
        if (StringUtils.isEmpty(conversationId)) {
            query = PromptConfig.PROMPT + "\n" + query;
        }

        String docContent = null;

        String url = chatbotRequest.getFileUrl();
        if(StringUtils.isNotBlank(url)) {
            String filePath = difyManager.getFilePathByUrl(url);
            docContent = difyManager.getFileContentByFilePath(filePath);

        }

        query = docContent + "\n" + query;

        log.info("content : {}", query);
        // todo 从数据库中查询
//        chatbotRequest.setPrompt("你是青青草原懒羊羊，也称懒大王");

        DifyRequest difyRequest = difyManager.getDifyRequest(chatbotRequest);
        DifyResponse difyResponse = new DifyResponse();

        if("llm".equals(difyConfig.getLlmType())) {
            difyResponse = difyManager.cattDifyBlock(difyRequest, conversationId, userId);
        }
        if("codeflying".equals(difyConfig.getLlmType())) {
            difyResponse = difyManager.callDifyTenantBlock(difyRequest, conversationId, userId);
        }
        ChatResponse chatResponse = new ChatResponse();

        // 处理聊天气泡
        String answer = difyResponse.getAnswer();
        chatResponse.setConversionId(difyResponse.getConversationId());

        String[] split = answer.split("##########");
        if(split.length > 1) {
            chatResponse.setAnswer(difyResponse.getAnswer());

//            chatResponse.setQuestions();
        }



        // 在后台线程中模拟实时数据
        return chatResponse;
    }


    @PostMapping(value = "download")
    public BaseResponse downLoadWord(@RequestBody DownLoadFileRequest downLoadFileRequest) throws IOException {
        // 加载模板文件
        String content = downLoadFileRequest.getContent();
        if(StringUtils.isEmpty(content)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "回复内容不能为空");
        }
        XWPFTemplate template = difyService.generateWordXWPFTemplate(content);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            template.write(outputStream);
            // 设置 HTTP 响应头，指示浏览器下载文件
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chat_message.docx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            // 转换为 Base64 字符串
            String base64File = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            return ResultUtils.success(base64File);
        }

    }

}
