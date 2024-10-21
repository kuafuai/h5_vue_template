package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.mapper.ChangeManagerMapper;
import com.kuafu.web.service.IChangeManagerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("ChangeManager")
public class ChangeManagerServiceImpl extends ServiceImpl<ChangeManagerMapper, ChangeManager> implements IChangeManagerService {

    @Override
    public List<Map<String, Object>> queryStatusStatics() {
        Long userId = SecurityUtils.getUserId();
        return getBaseMapper().staticsChangeStatus(userId.intValue());
    }
}
