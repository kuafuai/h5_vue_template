package com.kuafu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.web.entity.ChangeManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChangeManagerMapper extends BaseMapper<ChangeManager> {

    List<Map<String,Object>> staticsChangeStatus(@Param("personId") Integer personId);

}
