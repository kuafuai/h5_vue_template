package com.kuafu.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.util.*;
import com.kuafu.flowable.domain.FlowProcDefDto;
import com.kuafu.flowable.domain.FlowTaskVo;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.web.entity.*;
import com.kuafu.web.flowable.ChangeManagerBusinessService;
import com.kuafu.web.service.*;
import com.kuafu.web.vo.ChangeManagerPageVO;
import com.kuafu.web.vo.ChangeManagerVO;
import com.kuafu.web.vo.ChangeShowKeyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/changeManager")
@Api(tags = {"变更管理"})
@Slf4j
public class ChangeManagerController {
    private final IFlowDefinitionService flowDefinitionService;
    private final ChangeManagerBusinessService changeManagerBusinessService;

    private final IChangeManagerService changeManagerService;
    private final IChangeManagerInfoService changeManagerInfoService;

    private final IApproveNodeService approveNodeService;
    private final IFormSettingService formSettingService;

    private final IChangeTakeRecordAllService changeTakeRecordAllService;
    private final IChangeShowKeyService changeShowKeyService;

    private final IUserInfoService userInfoService;

    private final IChangeFollowService followService;

    private final IChangeFollowAllService followAllService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody ChangeManagerPageVO pageVO) {
        UserInfo userInfo = userInfoService.getById(SecurityUtils.getUserId());
        if ((userInfo.getAdmin() == null || !userInfo.getAdmin()) && (userInfo.getAdminReadOnly() == null || !userInfo.getAdminReadOnly()) && (userInfo.getChangePerson() == null || !userInfo.getChangePerson())) {
            return ResultUtils.error("您暂无权限，请联系管理员，设置权限");
        }

        IPage<ChangeManager> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeManager> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(pageVO.getChangeTitle())) {
            queryWrapper.like(ChangeManager::getChangeTitle, pageVO.getChangeTitle());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeCustomer())) {
            queryWrapper.like(ChangeManager::getChangeCustomer, pageVO.getChangeCustomer());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeProjectName())) {
            queryWrapper.like(ChangeManager::getChangeProjectName, pageVO.getChangeProjectName());
        }

        if ((userInfo.getAdmin() == null || !userInfo.getAdmin()) &&
                (userInfo.getAdminReadOnly() == null || !userInfo.getAdminReadOnly())) {
            // 不是管理员 并且不是 只读管理员
            if (userInfo.getChangePerson() != null && userInfo.getChangePerson()) {
                //如果是 发起变更人
                queryWrapper.eq(ChangeManager::getChangePerson, SecurityUtils.getUserId());
            }
        }

        queryWrapper.orderByDesc(ChangeManager::getChangeId);

        return ResultUtils.success(changeManagerService.page(page, queryWrapper));
    }

    @PostMapping("myTake")
    @ApiOperation("分页")
    public BaseResponse myTake(@RequestBody ChangeManagerPageVO pageVO) {
        IPage<ChangeTakeRecordAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeTakeRecordAll> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(pageVO.getChangeTitle())) {
            queryWrapper.like(ChangeTakeRecordAll::getChangeTitle, pageVO.getChangeTitle());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeCustomer())) {
            queryWrapper.like(ChangeTakeRecordAll::getChangeCustomer, pageVO.getChangeCustomer());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeProjectName())) {
            queryWrapper.like(ChangeTakeRecordAll::getChangeProjectName, pageVO.getChangeProjectName());
        }


        queryWrapper.eq(ChangeTakeRecordAll::getUserId, SecurityUtils.getUserId());
        queryWrapper.ne(ChangeTakeRecordAll::getChangePerson, SecurityUtils.getUserId());

        queryWrapper.orderByDesc(ChangeTakeRecordAll::getChangeId);

        return ResultUtils.success(changeTakeRecordAllService.page(page, queryWrapper));
    }

    @PostMapping("page-info")
    @ApiOperation("分页")
    public BaseResponse page_info(@RequestBody ChangeManagerPageVO pageVO) {

        UserInfo userInfo = userInfoService.getById(SecurityUtils.getUserId());
        if ((userInfo.getAdmin() == null || !userInfo.getAdmin()) && (userInfo.getAdminReadOnly() == null || !userInfo.getAdminReadOnly()) && (userInfo.getChangePerson() == null || !userInfo.getChangePerson())) {
            return ResultUtils.error("您暂无权限，请联系管理员，设置权限");
        }

        IPage<ChangeManager> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeManager> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(pageVO.getChangeTitle())) {
            queryWrapper.like(ChangeManager::getChangeTitle, pageVO.getChangeTitle());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeCustomer())) {
            queryWrapper.like(ChangeManager::getChangeCustomer, pageVO.getChangeCustomer());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeProjectName())) {
            queryWrapper.like(ChangeManager::getChangeProjectName, pageVO.getChangeProjectName());
        }

        if ((userInfo.getAdmin() == null || !userInfo.getAdmin()) &&
                (userInfo.getAdminReadOnly() == null || !userInfo.getAdminReadOnly())) {
            // 不是管理员 并且不是 只读管理员
            if (userInfo.getChangePerson() != null && userInfo.getChangePerson()) {
                //如果是 发起变更人
                queryWrapper.eq(ChangeManager::getChangePerson, SecurityUtils.getUserId());
            }
        }

        queryWrapper.orderByDesc(ChangeManager::getChangeId);
        page = changeManagerService.page(page, queryWrapper);

        for (ChangeManager changeManager : page.getRecords()) {
            LambdaQueryWrapper<ChangeManagerInfo> infoQuery = new LambdaQueryWrapper<>();
            infoQuery.eq(ChangeManagerInfo::getChangeId, changeManager.getChangeId());
            List<ChangeManagerInfo> listInfo = changeManagerInfoService.list(infoQuery);
            Map<String, Object> map = listInfo.stream().collect(Collectors.toMap(ChangeManagerInfo::getInfoKey, p -> p, (oldValue, newValue) -> newValue));

            changeManager.setInfoMap(map);
        }

        return ResultUtils.success(page);
    }

    @PostMapping("add")
    @ApiOperation("新增")
    public BaseResponse add(@RequestBody ChangeManagerVO vo) {
        UserInfo userInfo = userInfoService.getById(SecurityUtils.getUserId());
        if (userInfo.getChangePerson() != null && userInfo.getChangePerson()) {
            boolean flag = changeManagerBusinessService.processAddChangeManager(vo);
            return flag ? ResultUtils.success() : ResultUtils.error("发起变更失败");
        } else {
            return ResultUtils.error("您没有发起变更的权限！");
        }
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value = "id") Integer id) {
        ChangeManager entity = this.changeManagerService.getById(id);

        LambdaQueryWrapper<ChangeManagerInfo> infoQuery = new LambdaQueryWrapper<>();
        infoQuery.eq(ChangeManagerInfo::getChangeId, entity.getChangeId());
        List<ChangeManagerInfo> listInfo = changeManagerInfoService.list(infoQuery);
        Map<String, Object> map = listInfo.stream().collect(Collectors.toMap(ChangeManagerInfo::getInfoKey, p -> p, (oldValue, newValue) -> newValue));
        entity.setInfoMap(map);

        return entity != null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @GetMapping("print/{id}")
    public BaseResponse print(@PathVariable(value = "id") Integer id) {

        return ResultUtils.success(changeManagerBusinessService.print(id));
    }


    @GetMapping("get/showKey")
    public BaseResponse getShowKey() {
        LambdaQueryWrapper<ChangeShowKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChangeShowKey::getUserId, SecurityUtils.getUserId());

        List<ChangeShowKey> list = changeShowKeyService.list(queryWrapper);
        List<String> showKey = list.stream().map(ChangeShowKey::getShowKey).collect(Collectors.toList());

        Map<String, Object> result = Maps.newHashMap();
        result.put("showKey", list);
        result.put("selectKey", showKey);

        return ResultUtils.success(result);
    }

    @PostMapping("saveShowKey")
    public BaseResponse saveShowKey(@RequestBody ChangeShowKeyVO showKeyVO) {

        if (showKeyVO.getShowKeys() != null && !showKeyVO.getShowKeys().isEmpty()) {
            LambdaQueryWrapper<ChangeShowKey> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ChangeShowKey::getUserId, SecurityUtils.getUserId());
            changeShowKeyService.remove(queryWrapper);
            List<ChangeShowKey> list = Lists.newArrayList();
            for (String key : showKeyVO.getShowKeys()) {
                list.add(ChangeShowKey.builder().userId(SecurityUtils.getUserId().intValue()).showKey(key).showWidth("150").build());
            }
            changeShowKeyService.saveBatch(list);
        }

        return ResultUtils.success();
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
        List<Map<String, Object>> map = changeManagerService.queryStatusStatics();

        if (map != null && map.isEmpty()) {
            Map<String, Object> map1 = Maps.newHashMap();
            map1.put("change_status", 1);
            map1.put("number", 0);
            map.add(map1);


            Map<String, Object> map2 = Maps.newHashMap();
            map2.put("change_status", 2);
            map2.put("number", 0);
            map.add(map2);

            Map<String, Object> map3 = Maps.newHashMap();
            map3.put("change_status", 3);
            map3.put("number", 0);
            map.add(map3);
        }

        return ResultUtils.success(map);
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


    @PostMapping("/info/export")
    public void export(HttpServletResponse response, ChangeManagerPageVO pageVO) {
        UserInfo userInfo = userInfoService.getById(SecurityUtils.getUserId());
        if ((userInfo.getAdmin() == null || !userInfo.getAdmin()) &&
                (userInfo.getAdminReadOnly() == null || !userInfo.getAdminReadOnly()) &&
                (userInfo.getChangePerson() == null || !userInfo.getChangePerson())) {
            ServletUtils.renderString(response, JSON.toJSONString(ResultUtils.error("您暂无权限，请联系管理员，设置权限")));
        } else {

            LambdaQueryWrapper<ChangeManager> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(pageVO.getChangeTitle())) {
                queryWrapper.like(ChangeManager::getChangeTitle, pageVO.getChangeTitle());
            }
            if (StringUtils.isNotEmpty(pageVO.getChangeCustomer())) {
                queryWrapper.like(ChangeManager::getChangeCustomer, pageVO.getChangeCustomer());
            }
            if (StringUtils.isNotEmpty(pageVO.getChangeProjectName())) {
                queryWrapper.like(ChangeManager::getChangeProjectName, pageVO.getChangeProjectName());
            }

            if ((userInfo.getAdmin() == null || !userInfo.getAdmin()) &&
                    (userInfo.getAdminReadOnly() == null || !userInfo.getAdminReadOnly())) {
                // 不是管理员 并且不是 只读管理员
                if (userInfo.getChangePerson() != null && userInfo.getChangePerson()) {
                    //如果是 发起变更人
                    queryWrapper.eq(ChangeManager::getChangePerson, SecurityUtils.getUserId());
                }
            }

            List<ChangeManager> list = changeManagerService.list(queryWrapper);
            for (ChangeManager changeManager : list) {
                LambdaQueryWrapper<ChangeManagerInfo> infoQuery = new LambdaQueryWrapper<>();
                infoQuery.eq(ChangeManagerInfo::getChangeId, changeManager.getChangeId());
                List<ChangeManagerInfo> listInfo = changeManagerInfoService.list(infoQuery);
                Map<String, Object> map = listInfo.stream().collect(Collectors.toMap(ChangeManagerInfo::getInfoKey, p -> p, (oldValue, newValue) -> newValue));

                changeManager.setInfoMap(map);
            }

            ExcelUtils.export(list, response);

        }
    }

    /**
     * 获取 自动生成编码
     *
     * @param codeType
     * @return
     */
    @GetMapping("getCode")
    public BaseResponse getChangeCode(String codeType) {
        String code = "ECR编号";
        String type = "ECR";
        String value = "";
        if (StringUtils.equalsIgnoreCase(codeType, "ECR")) {
            code = "ECR编号";
            type = "ECR";
        } else {
            code = "ECN编号";
            type = "ECN";
        }

        String infoValue = changeManagerInfoService.getInfoValue(code);
        String today = DateUtils.dateTime();
        if (StringUtils.isNotEmpty(infoValue)) {
            String[] splitData = StringUtils.split(infoValue, "-");
            if (splitData.length > 1) {
                String date = splitData[1];
                if (StringUtils.equalsIgnoreCase(today, date)) {
                    int num = Integer.parseInt(splitData[2]);
                    num += 1;
                    String numStr = String.format("%03d", num);
                    value = type + "-" + today + "-" + numStr;
                } else {
                    value = type + "-" + today + "-" + "001";
                }

            } else {
                value = type + "-" + today + "-" + "001";
            }
        } else {
            value = type + "-" + today + "-001";
        }

        return ResultUtils.success(value);
    }

    @GetMapping("followPerson")
    public BaseResponse followPerson(@RequestParam(name = "changeId") Integer changeId,
                                     @RequestParam(name = "personId") Integer personId) {

        LambdaQueryWrapper<ChangeFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChangeFollow::getFollowChangeId, changeId);
        queryWrapper.eq(ChangeFollow::getFollowPersonId, personId);

        long count = followService.count(queryWrapper);
        if (count > 0) {
            return ResultUtils.success("已经添加过，请勿重复添加");
        } else {
            ChangeFollow changeFollow = new ChangeFollow();
            changeFollow.setFollowPersonId(personId);
            changeFollow.setFollowChangeId(changeId);

            followService.save(changeFollow);

            return ResultUtils.success("关注成功");
        }
    }

    @PostMapping("myFollow")
    @ApiOperation("分页")
    public BaseResponse myFollow(@RequestBody ChangeManagerPageVO pageVO) {
        IPage<ChangeFollowAll> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        LambdaQueryWrapper<ChangeFollowAll> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(pageVO.getChangeTitle())) {
            queryWrapper.like(ChangeFollowAll::getChangeTitle, pageVO.getChangeTitle());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeCustomer())) {
            queryWrapper.like(ChangeFollowAll::getChangeCustomer, pageVO.getChangeCustomer());
        }
        if (StringUtils.isNotEmpty(pageVO.getChangeProjectName())) {
            queryWrapper.like(ChangeFollowAll::getChangeProjectName, pageVO.getChangeProjectName());
        }


        queryWrapper.eq(ChangeFollowAll::getFollowPersonId, SecurityUtils.getUserId());

        queryWrapper.orderByDesc(ChangeFollowAll::getChangeId);

        return ResultUtils.success(followAllService.page(page, queryWrapper));
    }


}
