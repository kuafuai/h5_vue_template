package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class QyWxAppDepartmentResponse extends QyWxAppBaseResponse {

    @Expose
    @SerializedName("department")
    private List<QyWxAppDepartment> department;
}
