package com.kuafu.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.JSON;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.domain.FlowFormDto;
import com.kuafu.flowable.domain.FlowTaskVo;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.flowable.ChangeManagerBusinessService;
import com.kuafu.web.service.IChangeManagerService;
import com.kuafu.web.vo.ChangeManagerPageVO;
import com.kuafu.web.vo.ChangeManagerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/changeManager")
@Api(tags = {"变更管理"})
@Slf4j
public class ChangeManagerController {

    private final IChangeManagerService changeManagerService;

    private final IFlowTaskService flowTaskService;
    private final IFlowDefinitionService flowDefinitionService;

    private final ChangeManagerBusinessService changeManagerBusinessService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody ChangeManagerPageVO pageVO) {
        IPage<ChangeManager> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeManager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ChangeManager::getChangeId);

        return ResultUtils.success(changeManagerService.page(page, queryWrapper));
    }

    @PostMapping("add")
    @ApiOperation("新增")
    public BaseResponse add(@RequestBody ChangeManagerVO vo) {
        boolean flag = changeManagerBusinessService.processAddChangeManager(vo);
        return flag ? ResultUtils.success() : ResultUtils.error("发起变更失败");
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value = "id") Integer id) {
        ChangeManager entity = this.changeManagerService.getById(id);
        return entity != null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 完成 审批变更 节点
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/completeApprove")
    public BaseResponse completeApprove(@RequestBody FlowTaskVo flowTaskVo) {
        boolean flag = changeManagerBusinessService.completeApprove(flowTaskVo);
        return flag ? ResultUtils.success() : ResultUtils.error("操作失败");
    }

    /**
     * 完成提交物设置
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/completeSubmit")
    public BaseResponse completeSubmit(@RequestBody FlowTaskVo flowTaskVo) {
        boolean flag = changeManagerBusinessService.completeSubmit(flowTaskVo);
        return flag ? ResultUtils.success() : ResultUtils.error("操作失败");
    }

    /**
     * 上传提交物
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/completeSubmitUpload")
    public BaseResponse completeSubmitUpload(@RequestBody FlowTaskVo flowTaskVo) {
        boolean flag = changeManagerBusinessService.completeSubmitUpload(flowTaskVo);
        return flag ? ResultUtils.success() : ResultUtils.error("操作失败");
    }

    @PostMapping(value = "/completeCheckFile")
    public BaseResponse completeCheckFile(@RequestBody FlowTaskVo flowTaskVo) {
        boolean flag = changeManagerBusinessService.completeCheckFile(flowTaskVo);
        return flag ? ResultUtils.success() : ResultUtils.error("操作失败");
    }

    @GetMapping("changeRecords")
    public BaseResponse getFlowableRecord(String procInsId) {

        return ResultUtils.success(changeManagerBusinessService.getChangeFlowRecords(procInsId));
    }

    @GetMapping("myTodo")
    public BaseResponse myTodo() {

        return ResultUtils.success(changeManagerBusinessService.getTodoListByCurrentUser());
    }

    @GetMapping("flowFormData")
    public BaseResponse getFlowFormData(String changeType) {
        String deployId = "";
        String procDefId = "";
        if (StringUtils.equalsIgnoreCase(changeType, "ECR")) {
            deployId = "45001";
            procDefId = "ECR03:1:45004";
        } else {
            deployId = "47501";
            procDefId = "测试表单05:1:47504";
        }
        FlowFormDto formDto = flowTaskService.flowFormData(deployId);
        Map<String, Object> formJson = JSON.parseObject(formDto.getFormContent(), Map.class);

        Map<String, Object> result = Maps.newHashMap();
        result.put("deployId", deployId);
        result.put("procDefId", procDefId);
        result.put("formJson", formJson);

        return ResultUtils.success(result);
    }


}
