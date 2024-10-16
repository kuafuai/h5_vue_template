package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionMaterialVO {
    private Integer submissionId;

    private String submissionName;
    private Integer submissionType;

    private String submissionUserId;
    private String submissionUserName;
}
