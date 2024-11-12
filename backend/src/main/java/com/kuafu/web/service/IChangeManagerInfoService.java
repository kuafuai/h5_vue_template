package com.kuafu.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafu.web.entity.ChangeManagerInfo;

public interface IChangeManagerInfoService extends IService<ChangeManagerInfo> {
    String getInfoValue(String infoKey);
}
