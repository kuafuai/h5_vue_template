package com.kuafu.flowable.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowProcDefDto {

    private String id;

    @TableField(value="rd.name_")
    private String name;

    private String flowKey;

    private String category;

    private String formName;

    private Long formId;

    private int version;

    private String deploymentId;

    private int suspensionState;

    @TableField(value="rd.deploy_time_")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deploymentTime;
}
