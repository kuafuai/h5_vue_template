package com.kuafu.flowable.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.flowable.constant.FLowStop;
import com.kuafu.flowable.domain.*;
import org.flowable.bpmn.model.UserTask;

import java.util.List;
import java.util.Map;

public interface IFlowTaskService {

    /**
     * 审批任务
     *
     * @param task 请求实体参数
     */
    boolean complete(FlowTaskVo task);

    boolean complete(String taskId);

    /**
     * 驳回任务
     *
     * @param flowTaskVo
     */
    void taskReject(FlowTaskVo flowTaskVo);

    /**
     * 退回任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void taskReturn(FlowTaskVo flowTaskVo);

    /**
     * 获取所有可回退的节点
     *
     * @param flowTaskVo
     * @return
     */
    List<UserTask> findReturnTaskList(FlowTaskVo flowTaskVo);

    /**
     * 删除任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void deleteTask(FlowTaskVo flowTaskVo);

    /**
     * 认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void claim(FlowTaskVo flowTaskVo);

    /**
     * 取消认领/签收任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void unClaim(FlowTaskVo flowTaskVo);

    /**
     * 委派任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void delegateTask(FlowTaskVo flowTaskVo);

    /**
     * 任务归还
     *
     * @param flowTaskVo 请求实体参数
     */
    void resolveTask(FlowTaskVo flowTaskVo);

    /**
     * 转办任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void assignTask(FlowTaskVo flowTaskVo);


    /**
     * 多实例加签
     *
     * @param flowTaskVo
     */
    void addMultiInstanceExecution(FlowTaskVo flowTaskVo);

    /**
     * 多实例减签
     *
     * @param flowTaskVo
     */
    void deleteMultiInstanceExecution(FlowTaskVo flowTaskVo);

    /**
     * 取消申请
     * 目前实现方式: 直接将当前流程变更为已完成
     *
     * @param flowTaskVo
     * @return
     */
    void stopProcess(FlowTaskVo flowTaskVo);

    void stopProcess(FlowTaskVo flowTaskVo, FLowStop stop);

    /**
     * 撤回流程
     *
     * @param flowTaskVo
     * @return
     */
    void revokeProcess(FlowTaskVo flowTaskVo);

    /**
     * 我发起的流程
     *
     * @param queryVo 请求参数
     * @return
     */
    Page<FlowTaskDto> myProcess(FlowQueryVo queryVo);

    /**
     * 代办任务列表
     *
     * @param queryVo 请求参数
     * @return
     */
    Page<FlowTaskDto> todoList(FlowQueryVo queryVo);

    List<FlowTaskDto> todoListByUserId(String userId);

    /**
     * 已办任务列表
     *
     * @param queryVo 请求参数
     * @return
     */
    Page<FlowTaskDto> finishedList(FlowQueryVo queryVo);

    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例Id
     * @return
     */
    Map<String, Object> flowRecord(String procInsId, String deployId);

    /**
     * 流程历史流转记录
     *
     * @param procInsId
     * @return
     */
    List<FlowTaskDto> getFlowRecordByInsId(String procInsId);

    /**
     * 获取task 表单
     *
     * @param taskId
     * @return
     */
    String getTaskForm(String taskId);

    /**
     * 获取流程执行节点
     *
     * @param procInsId
     * @return
     */
    List<FlowViewerDto> getFlowViewer(String procInsId, String executionId);


    /**
     * 获取流程变量
     *
     * @param taskId
     * @return
     */
    Map<String, Object> processVariables(String taskId);


    /**
     * 获取下一节点
     *
     * @param flowTaskVo 任务
     * @return
     */
    FlowNextDto getNextFlowNode(FlowTaskVo flowTaskVo);

    FlowNextDto getNextFlowNodeByStart(FlowTaskVo flowTaskVo);

    /**
     * 获取 流程初始化表单
     *
     * @param deployId
     * @return
     */
    FlowFormDto flowFormData(String deployId);

    /**
     * 流程节点信息
     *
     * @param procInsId
     * @return
     */
    void flowXmlAndNode(String procInsId, String deployId);

    /**
     * 流程节点表单
     *
     * @param taskId 流程任务编号
     * @return
     */
    Map<String, Object> flowTaskForm(String taskId) throws Exception;


    /**
     * 流程节点信息
     *
     * @param procInsId
     * @param elementId
     * @return
     */
    void flowTaskInfo(String procInsId, String elementId);

}
