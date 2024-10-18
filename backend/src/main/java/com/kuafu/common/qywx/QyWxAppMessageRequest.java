package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kuafu.common.http.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class QyWxAppMessageRequest extends AbstractModel {

    @Expose(serialize = false, deserialize = false)
    public String accessToken;
}
