package com.kuafu.enums.controller;


import com.kuafu.common.domin.BaseResponse;
import com.kuafu.common.domin.ResultUtils;
import com.kuafu.enums.service.EnumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("enums")
@Api(tags = "枚举管理")
public class EnumsController {

    @Autowired
    private EnumService enumService;


    @GetMapping("/{key}")
    @ApiOperation("根据key获取枚举类型")
    public BaseResponse getEnums(@PathVariable() String key) {
        List enumsList = enumService.getEnumsList(key);
        return ResultUtils.success(enumsList);

    }

}
