package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class QyWxAppUserResponse extends QyWxAppBaseResponse {

    @Expose
    @SerializedName("userlist")
    private List<QyWxAppUser> userList;
}
