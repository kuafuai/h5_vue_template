package com.kuafu.message.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.kuafu.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName system_messagesVO
 */
@Data
public class SystemMessagesVO extends BaseEntity implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private String imageUrl;

    /**
     * 
     */
    private String url;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}