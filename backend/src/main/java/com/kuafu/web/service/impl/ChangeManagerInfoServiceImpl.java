package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeManagerInfo;
import com.kuafu.web.mapper.ChangeManagerInfoMapper;
import com.kuafu.web.service.IChangeManagerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ChangeManagerInfo")
public class ChangeManagerInfoServiceImpl extends ServiceImpl<ChangeManagerInfoMapper, ChangeManagerInfo>
        implements IChangeManagerInfoService {

    @Autowired
    private ChangeManagerInfoMapper changeManagerInfoMapper;

    @Override
    public String getInfoValue(String infoKey) {
       String infoValue = changeManagerInfoMapper.getInfoValue(infoKey);

        // 返回 info_value
        return infoValue != null ? infoValue : null;
    }
}
