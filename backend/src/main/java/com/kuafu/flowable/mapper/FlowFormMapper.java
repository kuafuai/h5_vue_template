package com.kuafu.flowable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuafu.flowable.domain.FlowFormDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FlowFormMapper extends BaseMapper<FlowFormDto> {

    FlowFormDto selectFormByDeployId(@Param("deployId") String deployId);

    FlowFormDto selectFormById(@Param("formId") Integer formId);
}
