package com.kuafu.common.qywx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class QyWxAppDepartment {

    @Expose
    @SerializedName("id")
    private Integer id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("name_en")
    private String nameEn;

    @Expose
    @SerializedName("parentid")
    private Integer parentId;
}
