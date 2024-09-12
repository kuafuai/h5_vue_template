package com.kuafu.login.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LoginReverence {

    AuthStatus("保存认证状态的信息", "AuthStatus"),
    ApplicationStatus("保存申请状态的信息", "ApplicationStatus"),
    ReviewStatus("保存审核状态的信息", "ReviewStatus"),
    CommunicationStatus("保存沟通状态的信息", "CommunicationStatus"),
    WechatExchangeStatus("保存交换微信状态的信息", "WechatExchangeStatus"),
    UserInfo("保存用户的信息", "UserInfo"),
    BuinessPerson("保存投资人信息", "BuinessPerson"),
    ProjectInfo("保存项目信息", "ProjectInfo"),
    ProjectFollowInfo("保存项目跟进信息", "ProjectFollowInfo"),
    FollowCommunicateInfo("保存项目跟进沟通记录", "FollowCommunicateInfo"),
    ContactInfo("保存联系记录", "ContactInfo"),
    ;


    private String label;
    private String value;

    LoginReverence(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public static List<SelectVo> all() {
        final LoginReverence[] values = LoginReverence.values();
        return Arrays.stream(values).map(r -> {
            return new SelectVo(r.getValue(), r.getLabel(), r.getLabel());
        }).collect(Collectors.toList());
    }
}
