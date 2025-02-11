package com.kuafu.enums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.enums.domain.Enumeration;
import com.kuafu.enums.mapper.EnumerationMapper;
import com.kuafu.enums.service.EnumerationService;
import org.springframework.stereotype.Service;

/**
* @author www.macpe.cn
* @description 针对表【enumeration(枚举表)】的数据库操作Service实现
* @createDate 2024-10-30 14:18:54
*/
@Service
public class EnumerationServiceImpl extends ServiceImpl<EnumerationMapper, Enumeration>
    implements EnumerationService {

}




