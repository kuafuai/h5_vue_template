package com.kuafu.flowable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kuafu.flowable.domain.FlowProcDefDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface IFlowDefinitionService {

    IPage<FlowProcDefDto> list(String name, Integer current, Integer pageSize);

    /**
     * 根据名称获取最近一条
     *
     * @param name
     * @return
     */
    FlowProcDefDto getLastByName(String name);

    /**
     * 导入流程文件
     * 当每个key的流程第一次部署时，指定版本为1。对其后所有使用相同key的流程定义，
     * 部署时版本会在该key当前已部署的最高版本号基础上加1。key参数用于区分流程定义
     *
     * @param name
     * @param category
     * @param in
     */
    void importByFile(String name, String category, InputStream in);

    /**
     * 导入流程文件
     * 当每个key的流程第一次部署时，指定版本为1。对其后所有使用相同key的流程定义，
     * 部署时版本会在该key当前已部署的最高版本号基础上加1。key参数用于区分流程定义
     *
     * @param name
     * @param category
     * @param content
     */
    void importByString(String name, String category, String content);

    String readXml(String deployId) throws IOException;

    InputStream readImage(String deployId);

    /**
     * 删除
     *
     * @param deployId
     */
    void delete(String deployId);

    /**
     * 修改状态
     *
     * @param state
     * @param deployId
     */
    void updateState(Integer state, String deployId);

    /**
     * 启动
     *
     * @param procDefId
     * @param variables
     * @return
     */
    String startProcessInstanceById(String procDefId, Map<String, Object> variables);
}
