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
@TableName("form_setting")
public class FormSetting {
    @TableId(value = "form_id", type = IdType.AUTO)
    private Integer formId;

    private String formName;
    private String formDescription;
    private String formContent;
}
