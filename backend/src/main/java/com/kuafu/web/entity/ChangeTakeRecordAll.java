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
public class ChangeTakeRecordAll {

    private Integer changeId;

    private String changeType;
    private String changeTitle;
    private String changeCustomer;
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

    @TableField(value="tr.user_id")
    private Integer userId;
}
