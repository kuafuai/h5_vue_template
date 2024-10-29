package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChangeManagerPageVO extends PageRequest {
    private Integer changeId;

    private String changeTitle;
    private String changeCustomer;
    private String changeProjectName;
    private String changeProductName;
    private String changeProjectStage;
    private Date changeStartTime;
    private Date changeEndTime;
    private String changePerson;
    private String changePersonName;
    private String flowableInstanceId;
    private Integer changeStatus;
    private String partNumber;
}
