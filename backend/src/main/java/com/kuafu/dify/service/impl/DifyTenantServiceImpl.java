package com.kuafu.dify.service.impl;

import com.deepoove.poi.XWPFTemplate;
import com.kuafu.dify.config.DifyConfig;
import com.kuafu.dify.entity.DifyFileVO;
import com.kuafu.dify.request.DifyRequest;
import com.kuafu.dify.service.DifyService;
import com.kuafu.llm.model.ChatResponse;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * 租户类Service
 */
public class DifyTenantServiceImpl implements DifyService {
    @Override
    public void callApiStream(DifyRequest difyRequest, String conversationId, String userId, SseEmitter sseEmitter) {

    }

    @Override
    public ChatResponse callApiBlock(DifyRequest difyRequest, String conversationId, String userId) {
        return null;
    }

    @Override
    public DifyFileVO callUpload(MultipartFile multipartFile, DifyConfig difyConfig) {
        return null;
    }

    @Override
    public XWPFTemplate generateWordXWPFTemplate(String answer) throws IOException {
        return null;
    }
}
