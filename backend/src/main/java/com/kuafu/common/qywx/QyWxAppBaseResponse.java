package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QyWxAppBaseResponse {
    @Expose
    @SerializedName("errcode")
    private Integer errcode;

    @Expose
    @SerializedName("errmsg")
    private String errmsg;
}
