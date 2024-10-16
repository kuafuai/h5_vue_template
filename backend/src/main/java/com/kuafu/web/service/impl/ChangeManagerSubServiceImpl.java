package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeManagerSub;
import com.kuafu.web.mapper.ChangeManagerSubMapper;
import com.kuafu.web.service.IChangeManagerSubService;
import org.springframework.stereotype.Service;

@Service("ChangeManagerSub")
public class ChangeManagerSubServiceImpl extends ServiceImpl<ChangeManagerSubMapper, ChangeManagerSub> implements IChangeManagerSubService {
}
