package com.kuafu.web.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeFollowAll {

    private Integer changeId;

    private String changeType;
    @TableField(value="cm.change_title")
    private String changeTitle;
    @TableField(value="cm.change_customer")
    private String changeCustomer;
    @TableField(value="cm.change_project_name")
    private String changeProjectName;

    private String changeProductName;
    private String changeProjectStage;
    private Date changeStartTime;
    private Date changeEndTime;

    @TableField(value="cm.change_person")
    private String changePerson;
    private String changePersonName;
    private String flowableInstanceId;

    private Integer changeStatus;

    @TableField(value="cf.follow_person_id")
    private Integer followPersonId;
}
