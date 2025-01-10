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
@TableName("change_follow")
public class ChangeFollow {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer followPersonId;
    private Integer followChangeId;
}
