package com.kuafu.web.handler;

import com.kuafu.common.login.LoginUser;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.util.ServletUtils;
import com.kuafu.login.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class TenantContextHolder {

    @Resource
    private TokenService tokenService;

    private static final boolean enableTenant = true;
    public static final String TENANT_TABLE_FIELD_NAME = "xxxxx";
    private static final Logger log = LoggerFactory.getLogger(CustomTenantHandler.class);

    public Integer getCurrentTenantId() {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return null;
        }

        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return null;
        }

        Integer tenantId = loginUser.getTenantId();
        log.info("Current Tenant ID is {}", tenantId);
        return tenantId;
    }

    public static boolean getEnableTenant() {
        return enableTenant;
    }
}
