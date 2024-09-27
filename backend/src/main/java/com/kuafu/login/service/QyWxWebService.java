package com.kuafu.login.service;


import com.kuafu.common.cache.Cache;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.common.qywx.*;
import com.kuafu.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "login", name = "enable")
public class QyWxWebService {

    @Value("${qywx.corp_id}")
    private String corpId;

    @Value("${qywx.corp_secret}")
    private String corpSecret;

    @Resource
    private Cache cache;
    private static final String KEY = "qywx:accesstoken";

    private QyWxAppClient client = new QyWxAppClient();

    public String getAccessToken() {
        String accessToken = cache.getCacheObject(KEY);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }

        QyWxAppAccessTokenRequest request = QyWxAppAccessTokenRequest.builder()
                .corpId(corpId)
                .corpSecret(corpSecret)
                .build();

        QyWxAppAccessTokenResponse response = client.getAccessToken(request);
        if (response.getErrcode() != null && response.getErrcode() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "token获取失败");
        }

        accessToken = response.getAccessToken();
        int expires_in = response.getExpiresIn();
        // 写入缓存中
        cache.setCacheObject(KEY, accessToken, expires_in, TimeUnit.SECONDS);

        return accessToken;
    }

    public String getUserByCode(String code) {
        QyWxAppCode2UserResponse response = client.getUserByCode(QyWxAppCode2UserRequest.builder()
                .accessToken(getAccessToken())
                .code(code)
                .build());
        if (response.getErrcode() != null && response.getErrcode() > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR.getCode(), "H5验证失败");
        }
        return response.getUserId();
    }
}
