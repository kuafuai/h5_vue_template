package com.kuafu.web.flowable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kuafu.common.file.FileUploadUtils;
import com.kuafu.common.file.FileUtils;
import com.kuafu.common.file.ImageUtils;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.shell.ShellExecutor;
import com.kuafu.common.util.DateUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.flowable.constant.FLowStop;
import com.kuafu.flowable.domain.FlowTaskDto;
import com.kuafu.flowable.domain.FlowTaskVo;
import com.kuafu.flowable.service.IFlowDefinitionService;
import com.kuafu.flowable.service.IFlowTaskService;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerInfo;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.exception.PersonException;
import com.kuafu.web.service.IChangeManagerInfoService;
import com.kuafu.web.service.IChangeManagerService;
import com.kuafu.web.service.IChangeManagerSubService;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.vo.ChangeManagerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChangeManagerBusinessService {

    @Autowired
    private IChangeManagerService changeManagerService;

    @Autowired
    private IFlowTaskService flowTaskService;

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IChangeManagerSubService changeManagerSubService;

    @Autowired
    private IChangeManagerInfoService changeManagerInfoService;

    @Transactional
    public boolean processAddChangeManager(ChangeManagerVO changeManagerVO) {
        String deployId = changeManagerVO.getDeployId();
        String procDefId = changeManagerVO.getProcDefId();
        Map<String, Object> variables = Maps.newHashMap();
        variables.putAll(changeManagerVO.getVariables());

        String procInsId = flowDefinitionService.startProcessInstanceById(procDefId, variables);

        UserInfo currentUserInfo = userInfoService.getById(SecurityUtils.getUserId());

        ChangeManager changeManager = ChangeManager.builder()
                .changeType(changeManagerVO.getChangeType())
                .changeTitle(changeManagerVO.getChangeTitle())
                .changeCustomer(changeManagerVO.getChangeCustomer())
                .changeProjectName(changeManagerVO.getChangeProjectName())
                .changeProductName(changeManagerVO.getChangeProductName())
                .changeStartTime(DateUtils.getNowDate())
                .changeEndTime(changeManagerVO.getChangeEndTime())
                .changePerson(currentUserInfo.getUserInfoId().toString())
                .changePersonName(currentUserInfo.getUserName())
                .changeProjectStage(changeManagerVO.getChangeProjectStage())
                .partNumber(changeManagerVO.getPartNumber())
                .flowableInstanceId(procInsId)
                .build();

        boolean flag = changeManagerService.save(changeManager);
        List<ChangeManagerInfo> infoList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : changeManagerVO.getVariables().entrySet()) {
            if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                Object value = entry.getValue();

                if (value instanceof String) {
                    ChangeManagerInfo info = ChangeManagerInfo.builder()
                            .changeId(changeManager.getChangeId())
                            .procInsId(procInsId)
                            .infoKey(entry.getKey())
                            .taskId("")
                            .infoValue(Objects.nonNull(value) ? value.toString() : "")
                            .build();
                    infoList.add(info);
                } else if (value instanceof List) {

                    List<Map<String, String>> listMap = (List) value;
                    StringBuilder infoValue = new StringBuilder();
                    for (Map<String, String> v_entry : listMap) {
                        if (v_entry.containsKey("url")) {
                            infoValue.append(v_entry.get("url")).append(";");
                        }
                    }

                    ChangeManagerInfo info = ChangeManagerInfo.builder()
                            .changeId(changeManager.getChangeId())
                            .procInsId(procInsId)
                            .infoKey(entry.getKey())
                            .taskId("")
                            .infoValue(infoValue.toString())
                            .build();
                    infoList.add(info);
                }
            }
        }

        changeManagerInfoService.saveBatch(infoList);
        return flag;
    }

    public List<FlowTaskDto> getChangeFlowRecords(String procInsId, boolean search_all) {
        if (StringUtils.isNotEmpty(procInsId)) {

            Map<String, FlowTaskDto> taskMap = Maps.newLinkedHashMap();

            LambdaQueryWrapper<ChangeManagerSub> subQueryWrapper = new LambdaQueryWrapper<>();
            subQueryWrapper.eq(ChangeManagerSub::getParentProcInsId, procInsId);

            List<ChangeManagerSub> subRecordInfo = changeManagerSubService.list(subQueryWrapper);
            Map<String, String> subTaskInfoMap = subRecordInfo
                    .stream()
                    .collect(Collectors.toMap(ChangeManagerSub::getParentTaskId, ChangeManagerSub::getSubProcInsId));

            // 查询提交的详情
            Map<String, ChangeManagerInfo> changeManagerInfoMap = Maps.newHashMap();
            if (search_all) {
                LambdaQueryWrapper<ChangeManagerInfo> infoQueryWrapper = new LambdaQueryWrapper<>();
                infoQueryWrapper.eq(ChangeManagerInfo::getProcInsId, procInsId);
                List<ChangeManagerInfo> infoList = changeManagerInfoService.list(infoQueryWrapper);
                changeManagerInfoMap = infoList.stream()
                        .filter(p -> StringUtils.isNotEmpty(p.getTaskId()))
                        .collect(Collectors.toMap(ChangeManagerInfo::getTaskId, p -> p));
            }


            List<FlowTaskDto> parentList = flowTaskService.getFlowRecordByInsId(procInsId);

            for (FlowTaskDto taskDto : parentList) {
                String taskName = taskDto.getTaskName();
                String taskId = taskDto.getTaskId();

                if (search_all) {
                    if (changeManagerInfoMap.containsKey(taskId)) {
                        taskDto.setInfoValue(changeManagerInfoMap.get(taskId).getInfoValue());
                    }
                }

                if (subTaskInfoMap.containsKey(taskId)) {
                    //子任务
                    FlowTaskDto parentTaskDto;
                    if (taskMap.containsKey(taskName)) {
                        parentTaskDto = taskMap.get(taskName);
                        parentTaskDto.setFinishTime(taskDto.getFinishTime());
                    } else {
                        //不存在
                        parentTaskDto = new FlowTaskDto();
                        parentTaskDto.setTaskName(taskName);
                        parentTaskDto.setHasChild(true);
                        parentTaskDto.setFinishTime(taskDto.getFinishTime());
                        taskMap.put(taskName, parentTaskDto);
                    }

                    String subProcInsId = subTaskInfoMap.get(taskId);
                    List<FlowTaskDto> subList = flowTaskService.getFlowRecordByInsId(subProcInsId);
                    if (search_all) {
                        for (FlowTaskDto subChild : subList) {
                            String subTaskId = subChild.getTaskId();
                            if (changeManagerInfoMap.containsKey(subTaskId)) {
                                subChild.setInfoValue(changeManagerInfoMap.get(subTaskId).getInfoValue());
                            }
                        }
                    }

                    taskDto.getSubTask().addAll(subList);

                    parentTaskDto.getSubTask().add(taskDto);

                } else {
                    //不是子任务
                    if (taskMap.containsKey(taskName)) {
                        //不是子任务，多实例任务
                        taskMap.get(taskName).getSubTask().add(taskDto);
                    } else {
                        taskMap.put(taskName, taskDto);
                    }
                }
            }

            return Lists.newArrayList(taskMap.values());
        } else {
            return Lists.newArrayList();
        }
    }


    public List<FlowTaskDto> getTodoListByCurrentUser() {
        Long userId = SecurityUtils.getUserId();


        List<FlowTaskDto> todoList = flowTaskService.todoListByUserId(userId.toString());
        for (FlowTaskDto taskDto : todoList) {
            String procInsId = taskDto.getProcInsId();

            LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
            parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);

            ChangeManager parentManager = changeManagerService.getOne(parentQuery);
            if (parentManager == null) {
                //说明是子任务
                LambdaQueryWrapper<ChangeManagerSub> subQuery = new LambdaQueryWrapper<>();
                subQuery.eq(ChangeManagerSub::getSubProcInsId, procInsId);
                ChangeManagerSub subTask = changeManagerSubService.getOne(subQuery);

                String parentProcInsId = subTask.getParentProcInsId();

                parentQuery = new LambdaQueryWrapper<>();
                parentQuery.eq(ChangeManager::getFlowableInstanceId, parentProcInsId);
                parentManager = changeManagerService.getOne(parentQuery);
            }
            taskDto.setProcInsId(parentManager.getFlowableInstanceId());
            taskDto.setChangeId(parentManager.getChangeId().toString());
            taskDto.setChangeTitle(parentManager.getChangeTitle());
            taskDto.setFinishTime(parentManager.getChangeEndTime());
        }

        return todoList;
    }


    @Transactional
    public boolean completeApprove(FlowTaskVo flowTaskVo) {
        if (flowTaskVo.getVariables() == null) {
            flowTaskVo.setVariables(Maps.newHashMap());
        }

        if (!flowTaskVo.getVariables().isEmpty()) {
            String procInsId = flowTaskVo.getInstanceId();

            LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
            parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);

            ChangeManager parentManager = changeManagerService.getOne(parentQuery);

            List<ChangeManagerInfo> infoList = Lists.newArrayList();
            for (Map.Entry<String, Object> entry : flowTaskVo.getVariables().entrySet()) {
                if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                    ChangeManagerInfo info = ChangeManagerInfo.builder()
                            .changeId(parentManager.getChangeId())
                            .procInsId(parentManager.getFlowableInstanceId())
                            .taskId(flowTaskVo.getTaskId())
                            .infoKey(entry.getKey())
                            .infoValue(entry.getValue().toString())
                            .build();

                    infoList.add(info);
                }
            }
            changeManagerInfoService.saveBatch(infoList);
        }

        return flowTaskService.complete(flowTaskVo);
    }

    /**
     * 完成提交物 节点
     *
     * @param flowTaskVo
     * @return
     */
    @Transactional
    public boolean completeSubmit(FlowTaskVo flowTaskVo) {
        int subTaskNumber = 0;
        if (flowTaskVo.getCheckSubmits() != null && !flowTaskVo.getCheckSubmits().isEmpty()) {
            subTaskNumber = flowTaskVo.getCheckSubmits().size();
        }

        if (subTaskNumber == 0) {
            return false;
        } else {
            if (flowTaskVo.getChoosePerson() == null || flowTaskVo.getChoosePerson().isEmpty()) {
                throw new PersonException("请正确填写提交人");
            }
            List<Map<String, Object>> subTasks = Lists.newArrayList();
            for (String checkSubmit : flowTaskVo.getCheckSubmits()) {
                if (flowTaskVo.getChoosePerson().containsKey(checkSubmit)) {
                    Map<String, Object> map = (Map<String, Object>) flowTaskVo.getChoosePerson().get(checkSubmit);
                    map.put("title", checkSubmit);
                    subTasks.add(map);
                } else {
                    log.error("{} has no person information", checkSubmit);
                    return false;
                }
            }

            Map<String, Object> taskMap = Maps.newHashMap();

            taskMap.put("sub_number", subTaskNumber);
            taskMap.put("subTasks", subTasks);

            flowTaskVo.setVariables(taskMap);

            return flowTaskService.complete(flowTaskVo);
        }
    }


    @Transactional
    public boolean completeSubmitUpload(FlowTaskVo flowTaskVo) {

        if (flowTaskVo.getVariables() == null) {
            flowTaskVo.setVariables(Maps.newHashMap());
        }

        String procInsId = flowTaskVo.getInstanceId();
        //查询 sub
        LambdaQueryWrapper<ChangeManagerSub> subQuery = new LambdaQueryWrapper<>();
        subQuery.eq(ChangeManagerSub::getSubProcInsId, procInsId);
        ChangeManagerSub subTask = changeManagerSubService.getOne(subQuery);

        // 查询parent
        String parentProcInsId = subTask.getParentProcInsId();

        LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
        parentQuery.eq(ChangeManager::getFlowableInstanceId, parentProcInsId);

        ChangeManager parentManager = changeManagerService.getOne(parentQuery);

        List<ChangeManagerInfo> infoList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : flowTaskVo.getVariables().entrySet()) {
            if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                ChangeManagerInfo info = ChangeManagerInfo.builder()
                        .changeId(parentManager.getChangeId())
                        .procInsId(parentManager.getFlowableInstanceId())
                        .taskId(flowTaskVo.getTaskId())
                        .infoKey(entry.getKey())
                        .infoValue(entry.getValue().toString())
                        .build();

                infoList.add(info);
            }
        }
        changeManagerInfoService.saveBatch(infoList);

        return flowTaskService.complete(flowTaskVo);
    }

    /**
     * 验收报告
     *
     * @param flowTaskVo
     * @return
     */
    @Transactional
    public boolean completeCheckFile(FlowTaskVo flowTaskVo) {
        if (flowTaskVo.getVariables() == null) {
            flowTaskVo.setVariables(Maps.newHashMap());
        }

        String procInsId = flowTaskVo.getInstanceId();

        LambdaQueryWrapper<ChangeManager> parentQuery = new LambdaQueryWrapper<>();
        parentQuery.eq(ChangeManager::getFlowableInstanceId, procInsId);

        ChangeManager parentManager = changeManagerService.getOne(parentQuery);

        List<ChangeManagerInfo> infoList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : flowTaskVo.getVariables().entrySet()) {
            if (!StringUtils.equalsIgnoreCase(entry.getKey(), "formJson")) {
                ChangeManagerInfo info = ChangeManagerInfo.builder()
                        .changeId(parentManager.getChangeId())
                        .procInsId(parentManager.getFlowableInstanceId())
                        .taskId(flowTaskVo.getTaskId())
                        .infoKey(entry.getKey())
                        .infoValue(entry.getValue().toString())
                        .build();

                infoList.add(info);
            }
        }
        changeManagerInfoService.saveBatch(infoList);

        return flowTaskService.complete(flowTaskVo);
    }


    /**
     * 取消 流程
     *
     * @param flowTaskVo
     */
    public void stopProcess(FlowTaskVo flowTaskVo) {
        flowTaskService.stopProcess(flowTaskVo, FLowStop.reject);
    }

    public void rejectProcess(FlowTaskVo flowTaskVo) {
        flowTaskService.taskReject(flowTaskVo);
    }


    public void assignTask(String sourceUserId, String targetUserId) {
        List<FlowTaskDto> todoList = flowTaskService.todoAllListByUserId(sourceUserId);

        for (FlowTaskDto flowTaskDto : todoList) {

            FlowTaskVo taskVo = new FlowTaskVo();
            taskVo.setTaskId(flowTaskDto.getTaskId());
            taskVo.setAssignee(targetUserId);

            flowTaskService.assignTask(taskVo);
        }
    }

    public void print(Integer changeId, HttpServletResponse response) {
        //查询出基本信息
        ChangeManager entity = this.changeManagerService.getById(changeId);

        LambdaQueryWrapper<ChangeManagerInfo> infoQuery = new LambdaQueryWrapper<>();
        infoQuery.eq(ChangeManagerInfo::getChangeId, entity.getChangeId());
        List<ChangeManagerInfo> listInfo = changeManagerInfoService.list(infoQuery);
        Map<String, ChangeManagerInfo> map = listInfo.stream().collect(Collectors.toMap(ChangeManagerInfo::getInfoKey, p -> p, (oldValue, newValue) -> newValue));

        List<FlowTaskDto> parentList = flowTaskService.getFlowRecordByInsId(entity.getFlowableInstanceId());


        String html = String.format(getHtmlFormatter(), getBaseHtml(entity), getFormContent(map), getApprove(parentList));
        String filePathCommand = FileUploadUtils.writeHtml(html);

        ShellExecutor shellExecutor = ShellExecutor.execute("wkhtmltopdf " + filePathCommand, ".");
        shellExecutor.start();

        String[] filePaths = StringUtils.split(filePathCommand, " ");
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLengthLong(FileUtils.sizeOf(filePaths[1]));
            FileUtils.setAttachmentResponseHeader(response, entity.getChangeTitle() + ".pdf");
            FileUtils.writeBytes(filePaths[1], response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBaseHtml(ChangeManager entity) {
        String html = "<div class=\"plan_box\">基础信息</div>\n" +
                "    <div style=\"height: 10px;\"></div>\n" +
                "    <div class=\"plan_box\">\n" +
                "        <table style=\"width: 100%%;\">\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">变更标题</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">发起人</td>\n" +
                "                    <td>%s</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">客户名称</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">项目名称</td>\n" +
                "                    <td>%s</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">产品名称</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">项目阶段</td>\n" +
                "                    <td>%s</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">发起时间</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">断点时间</td>\n" +
                "                    <td>%s</td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>";
        String endTime = "自然切换";
        if (entity.getChangeEndTime() != null) {
            endTime = DateUtils.dateTime(entity.getChangeEndTime());
        }

        return String.format(html,
                entity.getChangeTitle(), entity.getChangePersonName(),
                entity.getChangeCustomer(), entity.getChangeProjectName(),
                entity.getChangeProductName(), entity.getChangeProjectStage(),
                DateUtils.dateTime(entity.getChangeStartTime()), endTime);
    }

    private String getFormContent(Map<String, ChangeManagerInfo> map) {

        String html = "<div style=\"height: 20px;\"></div>\n" +
                "    <div class=\"plan_box\"><span></span>扩展信息<span></span></div>\n" +
                "    <div style=\"height: 10px;\"></div>\n" +
                "    <div class=\"plan_box\">\n" +
                "        <table style=\"width: 100%%;\">\n" +
                "            <tbody>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">变更类型</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">开发阶段</td>\n" +
                "                    <td>%s</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">变更原因</td>\n" +
                "                    <td style=\"width: 20%%;\">%s</td>\n" +
                "                    <td class=\"title\">变更原因图片</td>\n" +
                "                    <td style=\"width: 60%%;\">" +
                "<div class=\"image_box\" >\n" +
                "                            \n" +
                "%s" +
                "                        </div>\n" +
                "</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">更改前说明</td>\n" +
                "                    <td style=\"width: 20%%;\">%s</td>\n" +
                "                    <td class=\"title\">更改前说明图片</td>\n" +
                "                    <td style=\"width: 60%%;\"> " +
                "<div class=\"image_box\" >\n" +
                "                            \n" +
                "%s" +
                "                        </div>\n" +
                "</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td class=\"title\">更改后说明</td>\n" +
                "                    <td style=\"width: 20%%;\">%s</td>\n" +
                "                    <td class=\"title\">更改后说明图片</td>\n" +
                "                    <td style=\"width: 60%%;\">\n" +
                "                        <div class=\"image_box\" >\n" +
                "%s" +
                "                            \n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>";

        String imageFormatter = "<img style=\"width: 30%%; height: 30%%;\" src=\"data:image/png;base64,%s\"  />";

        String bglx = "无";
        if (map.containsKey("变更类型")) {
            ChangeManagerInfo ci = map.get("变更类型");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                bglx = ci.getInfoValue();
            }
        }

        String kfjd = "无";
        if (map.containsKey("开发阶段")) {
            ChangeManagerInfo ci = map.get("开发阶段");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                kfjd = ci.getInfoValue();
            }
        }

        String bgyx = "无";
        if (map.containsKey("变更原因")) {
            ChangeManagerInfo ci = map.get("变更原因");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                bgyx = ci.getInfoValue();
            }
        }
        String bgyxtu = "无";
        if (map.containsKey("变更原因图片")) {
            ChangeManagerInfo ci = map.get("变更原因图片");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                String[] image = StringUtils.split(ci.getInfoValue(), ";");
                StringBuilder sb = new StringBuilder();
                for (String img : image) {
                    String base64 = ImageUtils.encodeBytesToBase64(img);

                    String imageValue = String.format(imageFormatter, base64);
                    sb.append(imageValue).append("\n");
                }
                bgyxtu = sb.toString();
            }
        }

        String ggqsm = "无";
        if (map.containsKey("更改前说明")) {
            ChangeManagerInfo ci = map.get("更改前说明");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                ggqsm = ci.getInfoValue();
            }
        }

        String ggqsmtp = "无";
        if (map.containsKey("更改前图片")) {
            ChangeManagerInfo ci = map.get("更改前图片");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                String[] image = StringUtils.split(ci.getInfoValue(), ";");
                StringBuilder sb = new StringBuilder();
                for (String img : image) {
                    String base64 = ImageUtils.encodeBytesToBase64(img);

                    String imageValue = String.format(imageFormatter, base64);
                    sb.append(imageValue).append("\n");
                }
                ggqsmtp = sb.toString();
            }
        }

        String gghsm = "无";
        if (map.containsKey("更改后说明")) {
            ChangeManagerInfo ci = map.get("更改后说明");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                gghsm = ci.getInfoValue();
            }
        }

        String gghsmtp = "无";
        if (map.containsKey("更改后图片")) {
            ChangeManagerInfo ci = map.get("更改后图片");
            if (StringUtils.isNotEmpty(ci.getInfoValue())) {
                String[] image = StringUtils.split(ci.getInfoValue(), ";");
                StringBuilder sb = new StringBuilder();
                for (String img : image) {
                    String base64 = ImageUtils.encodeBytesToBase64(img);

                    String imageValue = String.format(imageFormatter, base64);
                    sb.append(imageValue).append("\n");
                }
                gghsmtp = sb.toString();
            }
        }

        return String.format(html,
                bglx, kfjd,
                bgyx, bgyxtu,
                ggqsm, ggqsmtp,
                gghsm, gghsmtp);
    }

    private String getApprove(List<FlowTaskDto> list) {
        String html = "<div style=\"height: 20px;\"></div>\n" +
                "    <div class=\"plan_box\"><span></span>审核信息<span></span></div>\n" +
                "    <div style=\"height: 10px;\"></div>\n" +
                "    <div class=\"plan_box\">\n" +
                "        <table style=\"width: 100%%;\">\n" +
                "            <tbody>\n" +
                "                %s" +
                "            </tbody>\n" +
                "        </table>\n" +
                "    </div>";

        String trHtml = "<tr>\n" +
                "                    <td class=\"title\">审批节点</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">处理人</td>\n" +
                "                    <td>%s</td>\n" +
                "                    <td class=\"title\">处理意见</td>\n" +
                "                    <td>%s</td>\n" +
                "                </tr>";

        StringBuilder sb = new StringBuilder();
        for (FlowTaskDto taskDto : list) {
            String taskName = taskDto.getTaskName();
            String assigneeName = taskDto.getAssigneeName();
            String comment = "无";
            if (taskDto.getComment() != null) {
                comment = taskDto.getComment().getComment();
            }
            sb.append(String.format(trHtml, taskName, assigneeName, comment));
        }

        return String.format(html, sb.toString());
    }

    private String getHtmlFormatter() {
        return "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>打印</title>\n" +
                "    <meta content=\"print\" name=\"description\"></meta>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "%s" +
                "%s    " +
                "\n" +
                "%s" +
                "\n" +
                "  </body> \n" +
                "  <style>\n" +
                "    .image_box {\n" +
                "        display: flex;\n" +
                "        justify-content: start;\n" +
                "        align-items: center;\n" +
                "    }\n" +
                "    .plan_box {\n" +
                "        overflow: hidden;\n" +
                "        display: flex;\n" +
                "        align-items: center;\n" +
                "        justify-content: center;\n" +
                "    }\n" +
                "    .plan_box table {\n" +
                "        border-spacing: 0;\n" +
                "        border-collapse: collapse;\n" +
                "    }\n" +
                "    .plan_box table thead tr:nth-child(2n+1) {\n" +
                "        background-color: #FAFAFA;\n" +
                "    }\n" +
                "\n" +
                "    th,td {\n" +
                "        border: 1px solid #666666;\n" +
                "        text-align: center;\n" +
                "        padding: 20px 5px;\n" +
                "    }\n" +
                "\n" +
                "    .plan_box table tr {\n" +
                "        height: 40px;\n" +
                "        font-size: 14px;\n" +
                "        color: #666666;\n" +
                "        line-height: 14px;\n" +
                "        font-weight: 400;\n" +
                "    }\n" +
                "\n" +
                "    .plan_box table tr:nth-child(2n) {\n" +
                "        background-color: #FAFAFA;\n" +
                "    }\n" +
                "\n" +
                "    .plan_box table .head {\n" +
                "        font-size: 14px;\n" +
                "        color: #666666;\n" +
                "        line-height: 14px;\n" +
                "        font-weight: 600;\n" +
                "    }\n" +
                "\n" +
                "    .title {\n" +
                "        width: 200px;\n" +
                "        text-align: center;\n" +
                "        font-size: 14px;\n" +
                "        color: #666666;\n" +
                "        line-height: 14px;\n" +
                "        font-weight: 600;\n" +
                "    }\n" +
                "\n" +
                "  </style>  \n" +
                "</html>\n";
    }

}
