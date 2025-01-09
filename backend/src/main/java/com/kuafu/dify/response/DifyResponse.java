package com.kuafu.dify.response;

import com.kuafu.dify.entity.OutputItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * dify 响应类
 */
@Data
public class DifyResponse {

    private String answer;

    private Integer appId;

    private String conversationId;

    @ApiModelProperty("输出项")
    private List<OutputItem> outputs;
}
