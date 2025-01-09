package com.kuafu.dify.entity;

import lombok.Data;

/**
 * 文件返回类
 */
@Data
public class DifyFileVO {
    /**
     * ID
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 拓展名
     */
    private String extension;
    /**
     * 类型
     */
    private String mime_type;
    private String created_by;
    private Long  created_at;
}
