package com.kuafu.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer changeType;
    private String changeTitle;
    private String changeCustomer;
    private String changeProjectName;
    private String changeProductName;
    private String changeProjectStage;
    private Date changeStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date changeEndTime;
    private String changePerson;
    private String changePersonName;
    private String flowableInstanceId;
    private Integer changeStatus;
    private String partNumber;

    private String deployId;
    private String procDefId;
    private Map<String, Object> variables;
}
