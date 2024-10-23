package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeShowKey;
import com.kuafu.web.mapper.ChangeShowKeyMapper;
import com.kuafu.web.service.IChangeShowKeyService;
import org.springframework.stereotype.Service;

@Service("ChangeShowKey")
public class ChangeShowKeyServiceImpl extends ServiceImpl<ChangeShowKeyMapper, ChangeShowKey>
        implements IChangeShowKeyService {
}
