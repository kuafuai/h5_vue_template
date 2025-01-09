package com.kuafu.dify.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 输入项
 */
@Data
public class InputItem {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("字段Key")
    private String fieldKey;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("字段值")
    private String value;

    @ApiModelProperty("所有图片id")
    private List<String> uploadFileIds;
}

