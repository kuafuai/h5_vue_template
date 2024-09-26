package com.kuafu.qywx.controller;

import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.qywx.service.QyWxBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qywx")
@Slf4j
public class QyWxBusinessController {

    @Autowired
    private QyWxBusinessService qyWxBusinessService;

    @GetMapping("/user/sync")
    public BaseResponse sync() {
        return qyWxBusinessService.syncUser() ? ResultUtils.success() : ResultUtils.error("同步失败");
    }
}
