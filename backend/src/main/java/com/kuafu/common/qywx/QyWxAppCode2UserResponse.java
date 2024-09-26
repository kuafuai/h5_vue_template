package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QyWxAppCode2UserResponse extends QyWxAppBaseResponse{
    @Expose
    @SerializedName("userid")
    private String userId;

    @Expose
    @SerializedName("user_ticket")
    private String userTicket;
}
