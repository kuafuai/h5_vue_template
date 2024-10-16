package com.kuafu.flowable.controlller;


import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.flowable.service.IFlowInstanceService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flowable/instance")
@Api(tags = {"流程实例"})
@Slf4j
public class FlowInstanceController {

    private final IFlowInstanceService flowInstanceService;

    @DeleteMapping(value = "/delete/{instanceIds}")
    public BaseResponse delete(@PathVariable String[] instanceIds,
                               @RequestParam(required = false) String deleteReason) {
        for (String instanceId : instanceIds) {
            flowInstanceService.delete(instanceId, deleteReason);
        }
        return ResultUtils.success();
    }
}
