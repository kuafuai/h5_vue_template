package com.kuafu.dify.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * dify 请求类
 */
@Data
public class DifyRequest implements Serializable {
    @JsonProperty("inputs")
    private Map<String,Object> inputs;

    @JsonProperty("query")
    private String query;
    @JsonProperty("response_mode")
    private String responseMode;

    @JsonProperty("conversation_id")
    private String conversationId;

    @JsonProperty("user")
    private String userId;
}
