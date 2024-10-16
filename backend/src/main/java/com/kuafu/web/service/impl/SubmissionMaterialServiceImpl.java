package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.SubmissionMaterial;
import com.kuafu.web.mapper.SubmissionMaterialMapper;
import com.kuafu.web.service.ISubmissionMaterialService;
import org.springframework.stereotype.Service;

@Service("SubmissionMaterial")
public class SubmissionMaterialServiceImpl extends ServiceImpl<SubmissionMaterialMapper, SubmissionMaterial> implements ISubmissionMaterialService {
}
