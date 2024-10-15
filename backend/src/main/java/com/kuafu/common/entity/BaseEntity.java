package com.kuafu.common.entity;

import com.kuafu.common.domin.PageRequest;
import lombok.Data;

import java.util.List;

@Data
public class BaseEntity extends PageRequest {
    private List<Filter> andFilter;

    private List<Filter> orFilter;

    private List<OrderCondition> orderConditions;
}
