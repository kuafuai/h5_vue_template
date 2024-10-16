package com.kuafu.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("change_manager_sub")
public class ChangeManagerSub {

    @TableId(value = "sub_task_id", type = IdType.AUTO)
    private Integer subTaskId;

    private String parentProcInsId;
    private String subProcInsId;
    private String parentTaskId;
}
