package com.kuafu.login.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @TableName login
 */
@TableName(value = "login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Login implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer loginId;

    /**
     *
     */
    private String wxOpenId;

    /**
     *
     */
    private String phoneNumber;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private String userName;

    /**
     *
     */
    private String relevanceId;

    private String relevanceTable;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}