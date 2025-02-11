package com.kuafu.login.domain;

import com.kuafu.login.domain.SelectVo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LoginReverence {

            userInfo("创业者信息表","userInfo"),
        buinessPerson("投资人信息表","buinessPerson"),
        projectInfo("项目信息表","projectInfo"),
        projectFollowInfo("项目跟进信息表","projectFollowInfo"),
        followCommunicateInfo("项目跟进沟通记录表","followCommunicateInfo"),
        contactInfo("联系沟通记录表","contactInfo"),
        authStatus("认证状态表","authStatus"),
        applicationStatus("联系人申请状态表","applicationStatus"),
        reviewStatus("审核状态表","reviewStatus"),
        communicationStatus("沟通状态表","communicationStatus"),
        wechatExchangeStatus("交换微信联系方式状态","wechatExchangeStatus"),
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
            return new SelectVo(r.getValue(), r.getLabel(),r.getLabel());
        }).collect(Collectors.toList());
    }
}
