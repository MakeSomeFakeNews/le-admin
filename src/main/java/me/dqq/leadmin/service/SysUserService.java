package me.dqq.leadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.dto.SysUserDto;
import me.dqq.leadmin.entity.SysUser;
import me.dqq.leadmin.vo.SysUserVO;

import java.util.List;

public interface SysUserService extends IService<SysUser> {
    PageResult<SysUserVO> getList(Integer page, Integer size, String username, String nickname, Integer status);
    SysUserVO getDetail(Long id);
    void add(SysUserDto dto);
    void update(SysUserDto dto);
    void delete(List<Long> ids);
}
