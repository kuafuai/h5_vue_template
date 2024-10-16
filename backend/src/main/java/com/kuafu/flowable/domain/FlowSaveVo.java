package com.kuafu.flowable.domain;

import com.kuafu.common.domin.PageRequest;
import lombok.Data;

@Data
public class FlowSaveVo extends PageRequest {
    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程分类
     */
    private String category;

    /**
     * xml 文件
     */
    private String xml;
}
