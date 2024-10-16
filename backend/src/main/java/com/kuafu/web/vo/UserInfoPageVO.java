package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserInfoPageVO extends PageRequest {
    private Long userInfoId;

    private String userName;
    private String departmentName;
    private String userId;
    private String openUserid;

    private Boolean admin;
    private Boolean adminReadOnly;
    private Boolean changePerson;
}
