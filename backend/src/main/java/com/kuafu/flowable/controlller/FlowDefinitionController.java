package com.kuafu.flowable.controlller;


import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.flowable.domain.FlowSaveVo;
import com.kuafu.flowable.service.IFlowDefinitionService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flowable/definition")
@Api(tags = {"流程定义"})
@Slf4j
public class FlowDefinitionController {

    private final IFlowDefinitionService flowDefinitionService;

    @PostMapping("page")
    public BaseResponse page(@RequestBody FlowSaveVo pageVO) {

        return ResultUtils.success(flowDefinitionService.list(pageVO.getName(), (int) pageVO.getCurrent(), (int) pageVO.getPageSize()));

    }

    @PostMapping("/start/{procDefId}")
    public BaseResponse start(@PathVariable(value = "procDefId") String procDefId,
                              @RequestBody Map<String, Object> variables) {
        String procInsId = flowDefinitionService.startProcessInstanceById(procDefId, variables);
        return ResultUtils.success();
    }
}
