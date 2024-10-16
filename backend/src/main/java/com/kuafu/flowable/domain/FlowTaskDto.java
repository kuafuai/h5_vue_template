package com.kuafu.flowable.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class FlowTaskDto implements Serializable {
    /**
     * 任务编号
     */
    private String taskId;

    /**
     * 任务执行编号
     */
    private String executionId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务Key
     */
    private String taskDefKey;

    /**
     * 任务执行人Id
     */
    private Long assigneeId;
    /**
     * 任务执行人名称
     */
    private String assigneeName;

    /**
     * 流程发起人Id
     */
    private String startUserId;

    /**
     * 流程发起人名称
     */
    private String startUserName;

    private String category;

    private Object variables;
    private Object taskLocalVars;

    private String deployId;
    private String procDefId;
    private String procDefKey;
    private String procDefName;
    private int procDefVersion;

    private String procInsId;
    private String hisProcInsId;
    private String duration;

    private FlowCommentDto comment;

    /**
     * 候选执行人
     */
    private String candidate;
    private Date createTime;
    private Date finishTime;

    private boolean hasChild = false;
    private List<FlowTaskDto> subTask = new ArrayList<>();

    private String changeId;
    private String changeTitle;
}
