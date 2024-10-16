package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormSettingVO {
    private Integer formId;

    private String formName;
    private String formDescription;
    private String formContent;
}
