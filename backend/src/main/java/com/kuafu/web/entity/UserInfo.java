package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_info")
public class UserInfo {

    @TableId(value = "user_info_id", type = IdType.AUTO)
    private Long userInfoId;

    private String userName;
    private String departmentName;
    private String userId;
    private String openUserid;

    private Boolean admin;
    private Boolean adminReadOnly;
    private Boolean changePerson;
}
