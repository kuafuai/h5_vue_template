package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.ChangeManagerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChangeManagerInfoMapper extends BaseMapper<ChangeManagerInfo> {

    String getInfoValue(@Param("info_key") String infoKey);
}
