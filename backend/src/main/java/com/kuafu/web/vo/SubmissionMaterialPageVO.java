package com.kuafu.web.vo;

import com.kuafu.common.domin.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubmissionMaterialPageVO extends PageRequest {

    private Integer submissionId;

    private String submissionName;
    private Integer submissionType;

    private String submissionUserId;
    private String submissionUserName;
}
