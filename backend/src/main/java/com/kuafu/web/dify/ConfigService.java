package com.kuafu.web.dify;

import com.google.common.collect.Maps;
import com.kuafu.common.domin.ErrorCode;
import com.kuafu.common.exception.BusinessException;
import com.kuafu.dify.constant.DifyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConfigService {

    public Map<String, String> loadDifyConfig(Integer... appId){
        return Maps.newHashMap();
    }
}
