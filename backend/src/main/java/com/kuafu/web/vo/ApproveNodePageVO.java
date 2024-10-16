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
public class ApproveNodePageVO extends PageRequest {
    private Integer approveNodeId;

    private String approveNodeName;

    private Integer approveType;
    private String approveUserId;
    private String approveUserName;
}
