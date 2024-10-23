package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeTakeRecordAll;
import com.kuafu.web.mapper.ChangeTakeRecordAllMapper;
import com.kuafu.web.service.IChangeTakeRecordAllService;
import org.springframework.stereotype.Service;

@Service("ChangeTakeRecordAll")
public class ChangeTakeRecordAllServiceImpl extends ServiceImpl<ChangeTakeRecordAllMapper, ChangeTakeRecordAll>
        implements IChangeTakeRecordAllService {
}
