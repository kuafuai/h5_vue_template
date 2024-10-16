package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.FormSetting;
import com.kuafu.web.mapper.FormSettingMapper;
import com.kuafu.web.service.IFormSettingService;
import org.springframework.stereotype.Service;

@Service("FormSetting")
public class FormSettingServiceImpl extends ServiceImpl<FormSettingMapper, FormSetting> implements IFormSettingService {
}
