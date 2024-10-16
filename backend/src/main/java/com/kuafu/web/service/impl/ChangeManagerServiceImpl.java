package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.mapper.ChangeManagerMapper;
import com.kuafu.web.service.IChangeManagerService;
import org.springframework.stereotype.Service;

@Service("ChangeManager")
public class ChangeManagerServiceImpl extends ServiceImpl<ChangeManagerMapper, ChangeManager> implements IChangeManagerService {
}
