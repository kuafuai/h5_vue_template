package com.kuafu.login.domain;

import com.kuafu.login.domain.SelectVo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum LoginReverence {

            userInfo("用户信息管理","userInfo"),
        taskInfo("任务信息管理","taskInfo"),
        hazardInfo("隐患信息管理","hazardInfo"),
        acceptanceReport("验收报告管理","acceptanceReport"),
        enterpriseInformation("企业信息管理","enterpriseInformation"),
        categoryMenu("（基本信息）隐患依据管理","categoryMenu"),
        categoryResult("（基本信息）隐患依据管理","categoryResult"),
        hazardBasic("（基本信息）隐患依据管理","hazardBasic"),
        hazardCollect("（基本信息）隐患收藏管理","hazardCollect"),
        hazardLevelStatus("（基本信息）隐患等级管理","hazardLevelStatus"),
        hazardAcceptanceStatus("（基本信息）隐患验收状态管理","hazardAcceptanceStatus"),
        correlationBasisStatus("（基本信息）关联依据状态管理","correlationBasisStatus"),
        hazardTypeStatus("（基本信息）隐患类型管理","hazardTypeStatus"),
        rectificationProgressStatus("（基本信息）整改进度管理","rectificationProgressStatus"),
        userRoleStatus("（基本信息）整改进度管理","userRoleStatus"),
        correctionDeadlineEnums("（基本信息）整改期限管理","correctionDeadlineEnums"),
        checkTypeEnums("（基本信息）检查类型管理","checkTypeEnums"),
        taskStatusEnums("（基本信息）任务状态枚举管理","taskStatusEnums"),
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
