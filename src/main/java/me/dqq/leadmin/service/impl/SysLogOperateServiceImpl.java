package me.dqq.leadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.dqq.leadmin.entity.SysLogOperate;
import me.dqq.leadmin.mapper.SysLogOperateMapper;
import me.dqq.leadmin.service.SysLogOperateService;
import org.springframework.stereotype.Service;

@Service
public class SysLogOperateServiceImpl extends ServiceImpl<SysLogOperateMapper, SysLogOperate> implements SysLogOperateService {
}
