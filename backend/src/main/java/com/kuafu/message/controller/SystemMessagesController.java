package com.kuafu.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.QueryUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.message.domain.SystemMessages;
import com.kuafu.message.service.SystemMessagesService;
import com.kuafu.message.vo.SystemMessagesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/systemMessages")
@Api(tags = {"系统信息表"})
public class SystemMessagesController {

    private final SystemMessagesService systemMessagesService;


    @PostMapping("page")
    @ApiOperation("全字段分页")
    public BaseResponse page(@RequestBody SystemMessagesVO pageVO) {
        IPage<SystemMessages> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());

        final QueryWrapper<SystemMessages> objectQueryWrapper = new QueryWrapper<>();
        QueryUtils.getQueryWrapper(objectQueryWrapper, pageVO);
        LambdaQueryWrapper<SystemMessages> queryWrapper = objectQueryWrapper.lambda();

        if (pageVO.getType() != null) {
            queryWrapper.like(SystemMessages::getType, pageVO.getType());
        }
        if (StringUtils.isNotEmpty(pageVO.getTitle())) {
            queryWrapper.like(SystemMessages::getTitle, pageVO.getTitle());
        }
        if (StringUtils.isNotEmpty(pageVO.getContent())) {
            queryWrapper.like(SystemMessages::getContent, pageVO.getContent());
        }

        systemMessagesService.page(page, queryWrapper);

        return ResultUtils.success(page);
    }

    @PostMapping("list")
    @ApiOperation("全字段列表")
    public BaseResponse list(@RequestBody SystemMessagesVO pageVO) {
        LambdaQueryWrapper<SystemMessages> queryWrapper = new LambdaQueryWrapper<>();

        if (pageVO.getType() != null) {
            queryWrapper.like(SystemMessages::getType, pageVO.getType());
        }
        if (StringUtils.isNotEmpty(pageVO.getTitle())) {
            queryWrapper.like(SystemMessages::getTitle, pageVO.getTitle());
        }
        if (StringUtils.isNotEmpty(pageVO.getContent())) {
            queryWrapper.like(SystemMessages::getContent, pageVO.getContent());
        }
        return ResultUtils.success(systemMessagesService.list(queryWrapper));
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value = "id") Integer systemMessageId) {
        SystemMessages entity = this.systemMessagesService.getById(systemMessageId);
        return entity != null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

}
