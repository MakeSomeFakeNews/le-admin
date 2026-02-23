package me.dqq.leadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.dqq.leadmin.dto.SysMenuDto;
import me.dqq.leadmin.entity.SysMenu;
import me.dqq.leadmin.vo.MenuOptionVO;
import me.dqq.leadmin.vo.SysMenuVO;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenuVO> getList();
    SysMenuVO getDetail(Long id);
    void add(SysMenuDto dto);
    void update(SysMenuDto dto);
    void delete(List<Long> ids);
    List<MenuOptionVO> getMenuOptions();
}
