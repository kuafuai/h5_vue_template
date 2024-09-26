package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @TableId(value = "user_info_id", type = IdType.AUTO)
    private Integer userInfoId;

    private String userName;
    private String departmentName;
    private String userId;
    private String openUserid;

    private Integer admin;
    private Integer adminReadOnly;
    private Integer changePerson;
}
