package com.kuafu.flowable.domain;

import lombok.Data;

@Data
public class FlowQueryVo {

    private String name;
    private String startTime;
    private String endTime;

    private Integer current;
    private Integer pageSize;
}
