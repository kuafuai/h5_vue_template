package com.kuafu.dify.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 输出项
 */
@Data
public class OutputItem {
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("dify字段")
    private String fieldKey;

    @ApiModelProperty("返回值")
    private String value;
}
