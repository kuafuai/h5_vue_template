package com.kuafu.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.message.domain.SystemMessages;
import com.kuafu.message.mapper.SystemMessagesMapper;
import com.kuafu.message.service.SystemMessagesService;
import org.springframework.stereotype.Service;

/**
* @author mac
* @description 针对表【system_messages】的数据库操作Service实现
* @createDate 2024-12-18 15:36:47
*/
@Service
public class SystemMessagesServiceImpl extends ServiceImpl<SystemMessagesMapper, SystemMessages>
    implements SystemMessagesService {

}




