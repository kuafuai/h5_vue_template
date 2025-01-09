package com.kuafu.dify.entity;

import lombok.Data;

@Data
public class DownLoadFileRequest {
    /**
     * 回复内容
     */
    private String content;

    /**
     * 对话ID
     */
    private String conversationId;

    /**
     * dify 应用ID
     */
    private Integer difyId;
}
