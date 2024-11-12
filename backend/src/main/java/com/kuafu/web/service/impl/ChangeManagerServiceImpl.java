package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.common.login.SecurityUtils;
import com.kuafu.web.entity.ChangeManager;
import com.kuafu.web.entity.ChangeManagerInfo;
import com.kuafu.web.mapper.ChangeManagerInfoMapper;
import com.kuafu.web.mapper.ChangeManagerMapper;
import com.kuafu.web.service.IChangeManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("ChangeManager")
public class ChangeManagerServiceImpl extends ServiceImpl<ChangeManagerMapper, ChangeManager> implements IChangeManagerService {
    @Autowired
    private ChangeManagerInfoMapper changeManagerInfoMapper;
    @Autowired
    private ChangeManagerMapper changeManagerMapper;

    @Override
    public List<Map<String, Object>> queryStatusStatics() {
        Long userId = SecurityUtils.getUserId();
        return getBaseMapper().staticsChangeStatus(userId.intValue());
    }
    //将变更的详细信息从数据库中查出来
    @Override
    public ChangeManager getDetailsById(Integer id) {
        ChangeManager changeManager = changeManagerMapper.selectById(id);
        LambdaQueryWrapper<ChangeManagerInfo> infoQuery = new LambdaQueryWrapper<>();
        infoQuery.eq(ChangeManagerInfo::getChangeId, changeManager.getChangeId());
        List<ChangeManagerInfo> listInfo = changeManagerInfoMapper.selectList(infoQuery);
        Map<String, Object> map = listInfo.stream().collect(Collectors.toMap(ChangeManagerInfo::getInfoKey, p -> p, (oldValue, newValue) -> newValue));
        changeManager.setInfoMap(map);
        return changeManager;
    }
}
