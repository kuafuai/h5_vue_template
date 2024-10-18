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
@TableName("change_manager_info")
public class ChangeManagerInfo {

    @TableId(value = "change_info_id", type = IdType.AUTO)
    private Integer changeInfoId;

    private Integer changeId;

    private String procInsId;
    private String infoKey;
    private String infoValue;

    private Date createTime;
}
