package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
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
public class QyWxAppDepartmentRequest extends AbstractModel {

    @Expose(serialize = false, deserialize = false)
    private String accessToken;

    @Override
    protected void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "access_token", this.accessToken);
    }
}
