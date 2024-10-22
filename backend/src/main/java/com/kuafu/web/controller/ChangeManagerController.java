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
import com.kuafu.flowable.domain.FlowProcDefDto;
import com.kuafu.flowable.domain.FlowTaskVo;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.ApproveNode;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerInfo;
import com.kuafu.web.entity.FormSetting;
import com.kuafu.web.flowable.ChangeManagerBusinessService;
import com.kuafu.web.service.IApproveNodeService;
import com.kuafu.web.service.IChangeManagerInfoService;
import com.kuafu.web.service.IChangeManagerService;
import com.kuafu.web.service.IFormSettingService;
import com.kuafu.web.vo.ChangeManagerPageVO;
import com.kuafu.web.vo.ChangeManagerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/changeManager")
@Api(tags = {"变更管理"})
@Slf4j
public class ChangeManagerController {

    private final IFlowTaskService flowTaskService;
    private final IFlowDefinitionService flowDefinitionService;
    private final ChangeManagerBusinessService changeManagerBusinessService;

    private final IChangeManagerService changeManagerService;
    private final IChangeManagerInfoService changeManagerInfoService;

    private final IApproveNodeService approveNodeService;
    private final IFormSettingService formSettingService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody ChangeManagerPageVO pageVO) {
        IPage<ChangeManager> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeManager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ChangeManager::getChangeId);

        return ResultUtils.success(changeManagerService.page(page, queryWrapper));
    }

    @PostMapping("page-info")
    @ApiOperation("分页")
    public BaseResponse page_info(@RequestBody ChangeManagerPageVO pageVO) {
        IPage<ChangeManager> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeManager> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ChangeManager::getChangeId);
        page = changeManagerService.page(page, queryWrapper);

        for (ChangeManager changeManager : page.getRecords()) {
            LambdaQueryWrapper<ChangeManagerInfo> infoQuery = new LambdaQueryWrapper<>();
            infoQuery.eq(ChangeManagerInfo::getChangeId, changeManager.getChangeId());
            List<ChangeManagerInfo> listInfo = changeManagerInfoService.list(infoQuery);
            Map<String, Object> map =
                    listInfo.stream()
                            .collect(Collectors.toMap(ChangeManagerInfo::getInfoKey, p -> p, (oldValue, newValue) -> newValue));

            changeManager.setInfoMap(map);
        }

        return ResultUtils.success(page);
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

    /**
     * 上传 验收报告
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/completeCheckFile")
    public BaseResponse completeCheckFile(@RequestBody FlowTaskVo flowTaskVo) {
        boolean flag = changeManagerBusinessService.completeCheckFile(flowTaskVo);
        return flag ? ResultUtils.success() : ResultUtils.error("操作失败");
    }

    /**
     * 任务详情
     *
     * @param procInsId
     * @return
     */
    @GetMapping("changeRecords")
    public BaseResponse getFlowableRecord(String procInsId) {

        return ResultUtils.success(changeManagerBusinessService.getChangeFlowRecords(procInsId, false));
    }

    @GetMapping("changeRecordsAll")
    public BaseResponse getFlowableRecordAll(String procInsId) {

        return ResultUtils.success(changeManagerBusinessService.getChangeFlowRecords(procInsId, true));
    }

    /**
     * 我的任务
     *
     * @return
     */
    @GetMapping("myTodo")
    public BaseResponse myTodo() {

        return ResultUtils.success(changeManagerBusinessService.getTodoListByCurrentUser());
    }

    @GetMapping("myStatics")
    public BaseResponse staticsStatus() {
        return ResultUtils.success(changeManagerService.queryStatusStatics());
    }

    @GetMapping("flowFormData")
    public BaseResponse getFlowFormData(Integer changeType) {

        FormSetting formSetting = formSettingService.getById(changeType);
        if (formSetting == null) {
            return ResultUtils.error("请先配置表单");
        }
        LambdaQueryWrapper<ApproveNode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApproveNode::getApproveNodeName, "审批变更");
        ApproveNode approveNode = approveNodeService.getOne(queryWrapper);
        if (approveNode == null) {
            return ResultUtils.error("请先配置 审批变更 节点");
        }
        String strApproveType = approveNode.getApproveType() == 1 ? "Parallel" : "Sequential";
        String formName = formSetting.getFormName();

        String flowableName = formName + "_" + strApproveType;

        FlowProcDefDto flowProcDefDto = flowDefinitionService.getLastByName(flowableName);
        if (flowProcDefDto == null) {
            return ResultUtils.error("请先配置变更流程");
        }

        Map<String, Object> result = Maps.newHashMap();
        result.put("deployId", flowProcDefDto.getDeploymentId());
        result.put("procDefId", flowProcDefDto.getId());
        result.put("formJson", JSON.parseObject(formSetting.getFormContent(), Map.class));

        return ResultUtils.success(result);
    }

    /**
     * 取消申请
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/stop")
    public BaseResponse stopChangeManager(@RequestBody FlowTaskVo flowTaskVo) {
        changeManagerBusinessService.stopProcess(flowTaskVo);
        return ResultUtils.success();
    }

    /**
     * 驳回上一级
     *
     * @param flowTaskVo
     * @return
     */
    @PostMapping(value = "/reject")
    public BaseResponse rejectChangeManager(@RequestBody FlowTaskVo flowTaskVo) {
        changeManagerBusinessService.rejectProcess(flowTaskVo);
        return ResultUtils.success();
    }

}
