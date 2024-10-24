package com.kuafu.web.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCreateEvent {
    private String taskName;
    private String procInsId;
    //分配人
    private String assignee;
    private String taskId;
}
