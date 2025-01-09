package com.kuafu.dify.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 调用 dify
 */
@Data
public class ChatbotRequest implements Serializable {
    @ApiModelProperty("聊天query")
    private String query;

    @ApiModelProperty("对话ID")
    private String conversationId;

    @ApiModelProperty("prompt")
    private String prompt;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("文件地址")
    private String fileUrl;
}
