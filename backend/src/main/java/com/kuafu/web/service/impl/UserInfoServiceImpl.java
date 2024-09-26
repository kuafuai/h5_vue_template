package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.UserInfo;
import com.kuafu.web.mapper.UserInfoMapper;
import com.kuafu.web.service.IUserInfoService;
import org.springframework.stereotype.Service;


@Service("UserInfo")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
}
