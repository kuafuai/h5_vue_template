package com.kuafu.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuafu.web.entity.ApproveNode;
import com.kuafu.web.mapper.ApproveNodeMapper;
import com.kuafu.web.service.IApproveNodeService;
import org.springframework.stereotype.Service;

@Service("ApproveNode")
public class ApproveNodeServiceImpl extends ServiceImpl<ApproveNodeMapper, ApproveNode> implements IApproveNodeService {
}
