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
@TableName("approve_node")
public class ApproveNode {
    @TableId(value = "approve_node_id", type = IdType.AUTO)
    private Integer approveNodeId;

    private String approveNodeName;

    private Integer approveType;
    private String approveUserId;
    private String approveUserName;
}
