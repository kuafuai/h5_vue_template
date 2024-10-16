package com.kuafu.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.web.entity.SubmissionMaterial;
import com.kuafu.web.service.ISubmissionMaterialService;
import com.kuafu.web.vo.SubmissionMaterialPageVO;
import com.kuafu.web.vo.SubmissionMaterialVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/submissionMaterial")
@Api(tags = {"提交物"})
public class SubmissionMaterialController {

    private final ISubmissionMaterialService submissionMaterialService;

    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody SubmissionMaterialPageVO pageVO) {
        IPage<SubmissionMaterial> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<SubmissionMaterial> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(pageVO.getSubmissionName())) {
            queryWrapper.like(SubmissionMaterial::getSubmissionName, pageVO.getSubmissionName());
        }
        return ResultUtils.success(submissionMaterialService.page(page, queryWrapper));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list(@RequestBody SubmissionMaterialVO vo) {
        LambdaQueryWrapper<SubmissionMaterial> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotEmpty(vo.getSubmissionName())) {
            queryWrapper.like(SubmissionMaterial::getSubmissionName, vo.getSubmissionName());
        }

        return ResultUtils.success(submissionMaterialService.list(queryWrapper));
    }

    @PostMapping("add")
    @ApiOperation("新增")
    public BaseResponse add(@RequestBody SubmissionMaterialVO vo) {
        SubmissionMaterial entity = SubmissionMaterial.builder()
                .submissionId(vo.getSubmissionId())
                .submissionName(vo.getSubmissionName())
                .submissionType(vo.getSubmissionType())
                .submissionUserId(vo.getSubmissionUserId())
                .submissionUserName(vo.getSubmissionUserName())
                .build();

        boolean flag = this.submissionMaterialService.save(entity);
        return flag ? ResultUtils.success(entity.getSubmissionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PutMapping("update")
    @ApiOperation("更新")
    public BaseResponse update(@RequestBody SubmissionMaterialVO vo) {
        SubmissionMaterial entity = SubmissionMaterial.builder()
                .submissionId(vo.getSubmissionId())
                .submissionName(vo.getSubmissionName())
                .submissionType(vo.getSubmissionType())
                .submissionUserId(vo.getSubmissionUserId())
                .submissionUserName(vo.getSubmissionUserName())
                .build();

        boolean flag = this.submissionMaterialService.saveOrUpdate(entity);
        return flag ? ResultUtils.success(entity.getSubmissionId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @GetMapping("get/{id}")
    @ApiOperation("根据Id查询")
    public BaseResponse get(@PathVariable(value = "id") Integer id) {
        SubmissionMaterial entity = this.submissionMaterialService.getById(id);
        return entity != null ? ResultUtils.success(entity) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

}
