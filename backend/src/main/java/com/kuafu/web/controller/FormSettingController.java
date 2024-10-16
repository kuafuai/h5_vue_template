package com.kuafu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.FormSetting;
import com.kuafu.web.service.IFormSettingService;
import com.kuafu.web.vo.FormSettingPageVO;
import com.kuafu.web.vo.FormSettingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/formSetting")
@Api(tags = {"表单"})
public class FormSettingController {

    private final IFormSettingService formSettingService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody FormSettingPageVO pageVO) {
        IPage<FormSetting> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<FormSetting> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(pageVO.getFormName())) {
            queryWrapper.like(FormSetting::getFormName, pageVO.getFormName());
        }

        return ResultUtils.success(formSettingService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list(@RequestBody FormSettingVO vo) {
        LambdaQueryWrapper<FormSetting> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(vo.getFormName())) {
            queryWrapper.like(FormSetting::getFormName, vo.getFormName());
        }
        return ResultUtils.success(formSettingService.list(queryWrapper));
    }

    @PutMapping("update")
    @ApiOperation("更新")
    public BaseResponse update(@RequestBody FormSettingVO vo) {
        FormSetting entity = FormSetting.builder()
                .formId(vo.getFormId())
                .formName(vo.getFormName())
                .formDescription(vo.getFormDescription())
                .formContent(vo.getFormContent())
                .build();

        boolean flag = this.formSettingService.saveOrUpdate(entity);
        return flag ? ResultUtils.success(entity.getFormId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value = "id") Integer id) {
        FormSetting entity = this.formSettingService.getById(id);
        return entity != null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }
}
