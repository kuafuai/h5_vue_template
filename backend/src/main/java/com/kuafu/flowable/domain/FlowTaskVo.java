package com.kuafu.flowable.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlowTaskVo {
    private String taskId;
    private String userId;
    private String comment;

    private String instanceId;
    private String targetKey;

    private String deploymentId;

    private String defId;

    private String currentChildExecutionId;

    private Boolean flag;

    private Map<String, Object> variables;
    private String assignee;

    private List<String> candidateUsers;
    private List<String> candidateGroups;

    private List<String> checkSubmits;
    private Map<String, Object> choosePerson;
}
