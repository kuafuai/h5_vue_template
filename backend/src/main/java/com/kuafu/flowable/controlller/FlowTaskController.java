package com.kuafu.flowable.controlller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.JSON;
import com.kuafu.flowable.domain.FlowFormDto;
import com.kuafu.flowable.domain.FlowNextDto;
import com.kuafu.flowable.domain.FlowQueryVo;
import com.kuafu.flowable.domain.FlowTaskVo;
import com.kuafu.flowable.service.IFlowTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flowable/task")
@Api(tags = {"工作流任务管理"})
public class FlowTaskController {

    private final IFlowTaskService flowTaskService;


    @GetMapping(value = "/myProcess")
    public BaseResponse myProcess(FlowQueryVo queryVo) {

        return ResultUtils.success(flowTaskService.myProcess(queryVo));
    }

    @GetMapping(value = "/todoList")
    public BaseResponse todoList(FlowQueryVo queryVo) {
        return ResultUtils.success(flowTaskService.todoList(queryVo));
    }

    /**
     * 取消申请
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/stopProcess")
    public BaseResponse stopProcess(@RequestBody FlowTaskVo flowTaskVo) {
        flowTaskService.stopProcess(flowTaskVo);
        return ResultUtils.success();
    }


    /**
     * 流程历史流转记录
     *
     * @param procInsId
     * @param deployId
     * @return
     */
    @GetMapping(value = "/flowRecord")
    public BaseResponse flowRecord(String procInsId, String deployId) {
        return ResultUtils.success(flowTaskService.flowRecord(procInsId, deployId));
    }

    /**
     * 获取 流程定义表单
     *
     * @param deployId
     * @return
     */
    @GetMapping(value = "/flowFormData")
    public BaseResponse flowFormData(String deployId) {
        FlowFormDto formDto = flowTaskService.flowFormData(deployId);
        if (Objects.isNull(formDto)) {
            return ResultUtils.error("请先配置流程表单!");
        }
        return ResultUtils.success(JSON.parseObject(formDto.getFormContent(), Map.class));
    }

    @GetMapping(value = "/processVariables/{taskId}")
    public BaseResponse processVariables(@ApiParam(value = "流程任务Id") @PathVariable(value = "taskId") String taskId) {
        return ResultUtils.success(flowTaskService.processVariables(taskId));
    }


    /**
     * 流程节点表单
     *
     * @param taskId 流程任务编号
     * @return
     */
    @GetMapping("/flowTaskForm")
    public BaseResponse flowTaskForm(@RequestParam(value = "taskId", required = false) String taskId) throws Exception {
        return ResultUtils.success(flowTaskService.flowTaskForm(taskId));
    }

    /**
     * 获取开始节点后的一个节点
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/nextFlowNodeByStart")
    public BaseResponse getNextFlowNodeByStart(@RequestBody FlowTaskVo flowTaskVo) {
        FlowNextDto nextDto = flowTaskService.getNextFlowNodeByStart(flowTaskVo);
        if (Objects.isNull(nextDto)) {
            return ResultUtils.error("流程不存在节点，请检查节点!");
        }
        return ResultUtils.success(nextDto);
    }

    @PostMapping(value = "/complete")
    public BaseResponse complete(@RequestBody FlowTaskVo flowTaskVo) {
        boolean flag = flowTaskService.complete(flowTaskVo);
        return flag ? ResultUtils.success() : ResultUtils.error("任务不存在");
    }

}
