package com.kuafu.qywx.controller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.common.util.StringUtils;
import com.kuafu.qywx.service.QyWxBusinessService;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qywx")
@Slf4j
public class QyWxBusinessController {

    @Autowired
    private QyWxBusinessService qyWxBusinessService;

    @Autowired
    private IUserInfoService userInfoService;

    @GetMapping("/user/sync")
    public BaseResponse sync(@RequestParam(name = "token", required = false) String token) {
        if (StringUtils.isNotEmpty(token) && StringUtils.equalsIgnoreCase(token, "ad816f03547e2f50")) {
            return qyWxBusinessService.syncUser() ? ResultUtils.success() : ResultUtils.error("同步失败");
        } else {
            //如果位空判断用户id是否存在
            Long userId = SecurityUtils.getUserId();
            UserInfo userInfo = userInfoService.getById(userId);
            if (userInfo.getAdmin()) {
                return qyWxBusinessService.syncUser() ? ResultUtils.success() : ResultUtils.error("同步失败");
            } else {
                return ResultUtils.error("请注意，只能管理员操作！");
            }
        }
    }
}
