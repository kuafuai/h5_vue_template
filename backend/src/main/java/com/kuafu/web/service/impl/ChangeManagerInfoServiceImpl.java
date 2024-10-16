package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeManagerInfo;
import com.kuafu.web.mapper.ChangeManagerInfoMapper;
import com.kuafu.web.service.IChangeManagerInfoService;
import org.springframework.stereotype.Service;

@Service("ChangeManagerInfo")
public class ChangeManagerInfoServiceImpl extends ServiceImpl<ChangeManagerInfoMapper, ChangeManagerInfo>
        implements IChangeManagerInfoService {
}
