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
@TableName("submission_material")
public class SubmissionMaterial {
    @TableId(value = "submission_id", type = IdType.AUTO)
    private Integer submissionId;

    private String submissionName;
    private Integer submissionType;

    private String submissionUserId;
    private String submissionUserName;
}
