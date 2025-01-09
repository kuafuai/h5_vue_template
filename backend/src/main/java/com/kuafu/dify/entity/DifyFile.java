package com.kuafu.dify.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * dify 图片 or 文件类
 */
@Data
public class DifyFile {
    @JsonProperty(value = "type")
    private String type;

    @JsonAlias(value = "transfer_method")
    private String transfer_method;

/*    @JsonProperty(value = "url")
    private String url;*/

    @JsonProperty(value = "upload_file_id")
    private String upload_file_id;
}
