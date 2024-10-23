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
@TableName("change_take_record")
public class ChangeTakeRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer changeId;
    private String procInsId;
    private Integer userId;
}
