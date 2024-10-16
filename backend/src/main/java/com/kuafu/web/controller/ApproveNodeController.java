package com.kuafu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.ApproveNode;
import com.kuafu.web.service.IApproveNodeService;
import com.kuafu.web.vo.ApproveNodePageVO;
import com.kuafu.web.vo.ApproveNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/approveNode")
@Api(tags = {"审批人"})
public class ApproveNodeController {

    private final IApproveNodeService approveNodeService;


    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody ApproveNodePageVO pageVO) {
        IPage<ApproveNode> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<ApproveNode> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(pageVO.getApproveNodeName())) {
            queryWrapper.like(ApproveNode::getApproveNodeName, pageVO.getApproveNodeName());
        }
        return ResultUtils.success(approveNodeService.page(page, queryWrapper));
    }

    @PutMapping("update")
    @ApiOperation("更新")
    public BaseResponse update(@RequestBody ApproveNodeVO vo) {
        ApproveNode entity = ApproveNode.builder()
                .approveNodeId(vo.getApproveNodeId())
                .approveNodeName(vo.getApproveNodeName())
                .approveType(vo.getApproveType())
                .approveUserId(vo.getApproveUserId())
                .approveUserName(vo.getApproveUserName())
                .build();

        boolean flag = this.approveNodeService.saveOrUpdate(entity);
        return flag ? ResultUtils.success(entity.getApproveNodeId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value = "id") Integer id) {
        ApproveNode entity = this.approveNodeService.getById(id);
        return entity != null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }
}
