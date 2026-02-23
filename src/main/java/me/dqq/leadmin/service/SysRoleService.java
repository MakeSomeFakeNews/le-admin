package me.dqq.leadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.dto.SysRoleDto;
import me.dqq.leadmin.entity.SysRole;
import me.dqq.leadmin.vo.SysRoleVO;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    PageResult<SysRoleVO> getList(Integer page, Integer size, String name, Integer status);
    SysRoleVO getDetail(Long id);
    void add(SysRoleDto dto);
    void update(SysRoleDto dto);
    void delete(List<Long> ids);
    List<Long> getRoleMenuIds(Long roleId);
    void setRoleMenus(Long roleId, List<Long> menuIds);
}
