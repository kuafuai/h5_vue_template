package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kuafu.common.http.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QyWxAppAccessTokenRequest extends AbstractModel {

    @Expose
    @SerializedName("corpid")
    private String corpId;

    @Expose
    @SerializedName("corpsecret")
    private String corpSecret;

    @Override
    protected void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "corpid", this.corpId);
        this.setParamSimple(map, prefix + "corpsecret", this.corpSecret);
    }
}
