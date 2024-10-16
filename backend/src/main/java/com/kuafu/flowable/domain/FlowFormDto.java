package com.kuafu.flowable.domain;

import lombok.Data;

@Data
public class FlowFormDto {

    private Long formId;
    private String formName;
    private String formDescription;
    private String formContent;

}
