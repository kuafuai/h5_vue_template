package com.kuafu.enums.domain;


import com.fasterxml.jackson.annotation.JsonValue;
import com.kuafu.web.entity.SelectVo;

/**
 * 所有枚举实现这个类
 */
public interface ValueEnums {
    public String getValue();


    @JsonValue
    public SelectVo toJson();





}
