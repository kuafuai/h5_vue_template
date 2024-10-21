package com.kuafu.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuafu.web.entity.ChangeManager;

import java.util.List;
import java.util.Map;

public interface IChangeManagerService extends IService<ChangeManager> {

    List<Map<String,Object>> queryStatusStatics();
}
