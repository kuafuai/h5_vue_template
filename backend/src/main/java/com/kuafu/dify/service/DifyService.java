package com.kuafu.dify.service;

import com.deepoove.poi.XWPFTemplate;
import com.kuafu.dify.config.DifyConfig;
import com.kuafu.dify.entity.DifyFileVO;
import com.kuafu.dify.request.DifyRequest;
import com.kuafu.llm.model.ChatResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;


public interface DifyService {


    void callApiStream(DifyRequest difyRequest, String conversationId, String userId, SseEmitter sseEmitter);

    ChatResponse callApiBlock(DifyRequest difyRequest, String conversationId, String userId);

    DifyFileVO callUpload(MultipartFile multipartFile, DifyConfig difyConfig);

    XWPFTemplate generateWordXWPFTemplate(String answer) throws IOException;

}
