package me.dqq.leadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.dqq.leadmin.entity.SysLogLogin;
import me.dqq.leadmin.mapper.SysLogLoginMapper;
import me.dqq.leadmin.service.SysLogLoginService;
import org.springframework.stereotype.Service;

@Service
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin> implements SysLogLoginService {
}
