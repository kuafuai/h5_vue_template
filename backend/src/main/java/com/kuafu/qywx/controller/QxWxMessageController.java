package com.kuafu.qywx.controller;


import com.kuafu.common.aes.AesException;
import com.kuafu.common.aes.WXBizJsonMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/msg")
@Slf4j
public class QxWxMessageController {

    @Value("${qywx.corp_id}")
    private String corpId;

    @Value("${qywx.message.token}")
    private String messageToken;

    @Value("${qywx.message.aes}")
    private String messageAesKey;

    @GetMapping("")
    public String check(HttpServletRequest request) throws AesException {
        // 获取企业微信推送的参数
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        log.info("{},{},{},{}", msgSignature, timestamp, nonce, echostr);

        WXBizJsonMsgCrypt wxBizJsonMsgCrypt = new WXBizJsonMsgCrypt(messageToken, messageAesKey, corpId);

        String echo = wxBizJsonMsgCrypt.VerifyURL(msgSignature, timestamp, nonce, echostr);
        log.info("===={}", echo);
        return echo;
    }
}
