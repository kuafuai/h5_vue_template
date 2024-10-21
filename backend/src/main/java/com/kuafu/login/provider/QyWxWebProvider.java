package com.kuafu.login.provider;

import com.kuafu.common.login.LoginUser;
import com.kuafu.common.util.StringUtils;
import com.kuafu.login.model.LoginVo;
import com.kuafu.login.service.LoginBusinessService;
import com.kuafu.login.service.QyWxWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "login", name = "enable")
public class QyWxWebProvider implements AuthenticationProvider {

    @Autowired
    private LoginBusinessService loginBusinessService;

    @Autowired
    private QyWxWebService qyWxWebService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LoginVo loginVo = (LoginVo) authentication.getPrincipal();
        log.info("{}", loginVo);
        String qyWxUserId;
        if (StringUtils.equalsIgnoreCase(loginVo.getCode(), "1111")) {
            qyWxUserId = "JiangFeiZhiHangHeYi";
        } else if (StringUtils.equalsIgnoreCase(loginVo.getCode(), "2222")) {
            qyWxUserId = "WuXinwuxin";
        } else if (StringUtils.equalsIgnoreCase(loginVo.getCode(), "3333")) {
            qyWxUserId = "leon";
        } else {
            qyWxUserId = qyWxWebService.getUserByCode(loginVo.getCode());
        }

        Object current = loginBusinessService.getUserByOpenId(qyWxUserId);
        if (current == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        Long userId = loginBusinessService.getId(current);
        final String relevanceTable = loginBusinessService.getValue(current, "relevanceTable").toString();
        String relevanceId = Optional.ofNullable(loginBusinessService.getValue(current, "relevanceId"))
                .map(Object::toString).orElse(null);

        if (StringUtils.isNotEmpty(relevanceId)) {
            userId = Long.valueOf(relevanceId);
        }

        return new QyWxWebAuthentication(new LoginUser(userId, relevanceId, relevanceTable), authentication.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(QyWxWebAuthentication.class);
    }
}
