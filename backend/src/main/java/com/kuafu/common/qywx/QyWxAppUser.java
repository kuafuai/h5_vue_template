package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QyWxAppUser {

    @Expose
    @SerializedName("userid")
    private String userId;

    @Expose
    @SerializedName("name")
    private String userName;

    @Expose
    @SerializedName("open_userid")
    private String openUserid;

    private String departmentName;
}
