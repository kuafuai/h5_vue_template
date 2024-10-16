package com.kuafu.flowable.service;

import org.flowable.engine.history.HistoricProcessInstance;

public interface IFlowInstanceService {

    /**
     * 激活或挂起流程实例
     *
     * @param state      状态
     * @param instanceId 流程实例ID
     */
    void updateState(Integer state, String instanceId);


    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    void delete(String instanceId, String deleteReason);

    /**
     * 根据实例ID查询历史实例数据
     *
     * @param processInstanceId
     * @return
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);

}
