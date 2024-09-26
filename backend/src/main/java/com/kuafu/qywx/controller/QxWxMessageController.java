package com.kuafu.qywx.controller;


import com.kuafu.common.aes.AesException;
import com.kuafu.common.aes.WXBizJsonMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/msg")
@Slf4j
public class QxWxMessageController {


    @GetMapping("")
    public String check(HttpServletRequest request) throws AesException {
        String sToken = "5J07lqDR79J8OZZCc6LreP";
        String sCorpID = "ww72adcae86ddaa0f3";
        String sEncodingAESKey = "dWSLWl9g6M9Hmy2auEePaBw19wrz8a8qosZ9Uj3QCCC";

        // 获取企业微信推送的参数
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        log.info("{},{},{},{}", msgSignature, timestamp, nonce, echostr);

        WXBizJsonMsgCrypt wxBizJsonMsgCrypt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);

        String echo = wxBizJsonMsgCrypt.VerifyURL(msgSignature, timestamp, nonce, echostr);
        log.info("===={}", echo);
        return echo;
    }
}
