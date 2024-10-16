package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeManagerVO {
    private Integer changeId;
    private String changeType;
    private String changeTitle;
    private String changeCustomer;
    private String changeProjectName;
    private String changeProductName;
    private Date changeStartTime;
    private Date changeEndTime;
    private String changePerson;
    private String changePersonName;
    private String flowableInstanceId;

    private String deployId;
    private String procDefId;
    private Map<String, Object> variables;
}
