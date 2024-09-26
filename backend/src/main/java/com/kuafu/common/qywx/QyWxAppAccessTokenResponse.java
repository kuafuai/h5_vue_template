package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QyWxAppAccessTokenResponse extends QyWxAppBaseResponse {
    @Expose
    @SerializedName("access_token")
    private String accessToken;

    @Expose
    @SerializedName("expires_in")
    private Integer expiresIn;
}
