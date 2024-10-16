package com.kuafu.login.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 微信小程序注册事物
 */
@Component
public class WechatRegisterService {
    @Resource
    private LoginBusinessService loginBusinessService;

    @Transactional
    public long wechatRegister(String phone, String relevanceTable) {
       return loginBusinessService.insertRelevanceInfo(phone, relevanceTable);
    }
}
