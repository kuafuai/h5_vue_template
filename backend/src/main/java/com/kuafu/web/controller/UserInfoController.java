package com.kuafu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.login.domain.SelectVo;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.flowable.ChangeManagerBusinessService;
import com.kuafu.web.service.IUserInfoService;
import com.kuafu.web.vo.UserInfoPageVO;
import com.kuafu.web.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userInfo")
@Api(tags = {"人员"})
public class UserInfoController {
    private final IUserInfoService userInfoService;

    private final ChangeManagerBusinessService changeManagerBusinessService;


    @PostMapping("page")
    @ApiOperation("分页")
    public BaseResponse page(@RequestBody UserInfoPageVO pageVO) {
        IPage<UserInfo> page = new Page<>(pageVO.getCurrent(), pageVO.getPageSize());
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(pageVO.getUserName())) {
            queryWrapper.like(UserInfo::getUserName, pageVO.getUserName());
        }

        return ResultUtils.success(userInfoService.page(page, queryWrapper));
    }


    @PostMapping("list")
    @ApiOperation("列表")
    public BaseResponse list(@RequestBody UserInfoVO vo) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(vo.getUserName())) {
            queryWrapper.like(UserInfo::getUserName, vo.getUserName());
        }

        return ResultUtils.success(userInfoService.list(queryWrapper));
    }


    @PutMapping("update")
    @ApiOperation("更新")
    public BaseResponse update(@RequestBody UserInfoVO vo) {
        UserInfo entity = UserInfo.builder()
                .userInfoId(vo.getUserInfoId())
                .userName(vo.getUserName())
                .departmentName(vo.getDepartmentName())
                .userId(vo.getUserId())
                .openUserid(vo.getOpenUserid())
                .admin(vo.getAdmin())
                .adminReadOnly(vo.getAdminReadOnly())
                .changePerson(vo.getChangePerson())
                .build();
        boolean flag = this.userInfoService.saveOrUpdate(entity);
        return flag ? ResultUtils.success(entity.getUserInfoId()) : ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }


    @PostMapping("assign")
    public BaseResponse assignTask(@RequestBody UserInfoVO vo) {
        if (vo.getUserInfoId() == null || vo.getAssigner() == null) {
            return ResultUtils.error("参数有误！");
        }
        if (vo.getUserInfoId().equals(vo.getAssigner())) {
            return ResultUtils.error("交接的人不能是同一人");
        }

        changeManagerBusinessService.assignTask(vo.getUserInfoId().toString(), vo.getAssigner().toString());

        return ResultUtils.success();
    }


    @GetMapping("get/select_list")
    @ApiOperation("查询下拉框的外键信息")
    public BaseResponse get_select_list() {
        List<UserInfo> list = this.userInfoService.list();
        final List<SelectVo> selectVoList = list.stream().map(p ->
                        new SelectVo(p.getUserInfoId(), p.getUserName() + "(" + p.getDepartmentName() + ")", p.getUserName() + "(" + p.getDepartmentName() + ")"))
                .collect(Collectors.toList());
        return ResultUtils.success(selectVoList);
    }
}
