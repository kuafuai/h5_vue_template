package com.kuafu.common.qywx;

import com.google.common.collect.Lists;
import com.kuafu.common.http.AbstractClient;
import com.kuafu.common.util.JSON;

import java.util.List;

public class QyWxAppClient extends AbstractClient {

    public QyWxAppClient() {
        super();
    }

    public QyWxAppAccessTokenResponse getAccessToken(QyWxAppAccessTokenRequest request) {
        String value = this.internalRequest("https://qyapi.weixin.qq.com/cgi-bin/gettoken", "GET", request);

        return JSON.parseObject(value, QyWxAppAccessTokenResponse.class);
    }

    public QyWxAppCode2UserResponse getUserByCode(QyWxAppCode2UserRequest request) {
        String value = this.internalRequest("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo", "GET", request);
        return JSON.parseObject(value, QyWxAppCode2UserResponse.class);
    }


    public List<QyWxAppDepartment> getDepartments(QyWxAppDepartmentRequest request) {
        String value = this.internalRequest("https://qyapi.weixin.qq.com/cgi-bin/department/list", "GET", request);

        QyWxAppDepartmentResponse response = JSON.parseObject(value, QyWxAppDepartmentResponse.class);
        if (response.getErrcode() == 0) {
            return response.getDepartment();
        } else {
            return Lists.newArrayList();
        }
    }

    public List<QyWxAppUser> getUsers(QyWxAppUserRequest request) {
        String value = this.internalRequest("https://qyapi.weixin.qq.com/cgi-bin/user/list", "GET", request);

        QyWxAppUserResponse response = JSON.parseObject(value, QyWxAppUserResponse.class);
        if (response.getErrcode() == 0) {
            return response.getUserList();
        } else {
            return Lists.newArrayList();
        }
    }
}
