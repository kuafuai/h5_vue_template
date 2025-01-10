package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeFollowAll;
import com.kuafu.web.mapper.ChangeFollowAllMapper;
import com.kuafu.web.service.IChangeFollowAllService;
import org.springframework.stereotype.Service;

@Service("ChangeFollowAll")
public class ChangeFollowAllServiceImpl extends ServiceImpl<ChangeFollowAllMapper, ChangeFollowAll>
        implements IChangeFollowAllService {
}
