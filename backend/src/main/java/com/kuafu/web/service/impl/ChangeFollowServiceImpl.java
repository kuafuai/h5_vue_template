package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeFollow;
import com.kuafu.web.mapper.ChangeFollowMapper;
import com.kuafu.web.service.IChangeFollowService;
import org.springframework.stereotype.Service;

@Service("ChangeFollow")
public class ChangeFollowServiceImpl extends ServiceImpl<ChangeFollowMapper, ChangeFollow> implements IChangeFollowService {
}
