package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("change_manager")
public class ChangeManager {
    @TableId(value = "change_id", type = IdType.AUTO)
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
}
