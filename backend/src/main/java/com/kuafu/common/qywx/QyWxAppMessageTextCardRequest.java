package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QyWxAppMessageTextCardRequest extends QyWxAppMessageRequest {

    @Expose
    @SerializedName("textcard")
    private TextCard textcard;

    @Expose
    @SerializedName("touser")
    private String touser;

    @Expose
    @SerializedName("msgtype")
    private String msgtype;

    @Expose
    @SerializedName("agentid")
    private Integer agentid;

    @Override
    protected void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "access_token", this.getAccessToken());
    }
}
