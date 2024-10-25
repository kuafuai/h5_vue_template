package com.kuafu.web.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {
    private Long userInfoId;

    private String userName;
    private String departmentName;
    private String userId;
    private String openUserid;

    private Boolean admin;
    private Boolean adminReadOnly;
    private Boolean changePerson;

    private Long assigner;
}
