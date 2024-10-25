package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ChangeTakeRecord;
import com.kuafu.web.mapper.ChangeTakeRecordMapper;
import com.kuafu.web.service.IChangeTakeRecordService;
import org.springframework.stereotype.Service;


@Service("ChangeTakeRecordService")
public class ChangeTakeRecordServiceImpl extends ServiceImpl<ChangeTakeRecordMapper, ChangeTakeRecord>
        implements IChangeTakeRecordService {

}
