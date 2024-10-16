package com.kuafu.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveNodeVO {
    private Integer approveNodeId;

    private String approveNodeName;

    private Integer approveType;
    private String approveUserId;
    private String approveUserName;
}
