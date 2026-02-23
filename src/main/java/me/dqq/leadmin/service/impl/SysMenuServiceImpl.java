package me.dqq.leadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.common.exception.BizException;
import me.dqq.leadmin.dto.SysMenuDto;
import me.dqq.leadmin.entity.SysMenu;
import me.dqq.leadmin.entity.SysRoleMenu;
import me.dqq.leadmin.mapper.SysMenuMapper;
import me.dqq.leadmin.mapper.SysRoleMapper;
import me.dqq.leadmin.mapper.SysRoleMenuMapper;
import me.dqq.leadmin.mapper.SysUserRoleMapper;
import me.dqq.leadmin.service.SysMenuService;
import me.dqq.leadmin.util.SecurityUtil;
import me.dqq.leadmin.vo.MenuOptionVO;
import me.dqq.leadmin.vo.SysMenuVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;

    @Override
    public List<SysMenuVO> getList() {
        List<SysMenu> menus = list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSort));
        List<SysMenuVO> voList = menus.stream().map(this::toVO).collect(Collectors.toList());
        return buildTree(voList);
    }

    @Override
    public SysMenuVO getDetail(Long id) {
        SysMenu menu = getById(id);
        if (menu == null) throw new BizException("菜单不存在");
        return toVO(menu);
    }

    @Override
    @Transactional
    public void add(SysMenuDto dto) {
        SysMenu menu = new SysMenu();
        copyDtoToEntity(dto, menu);
        menu.setCreateBy(SecurityUtil.getUserId());
        menu.setUpdateBy(SecurityUtil.getUserId());
        save(menu);
    }

    @Override
    @Transactional
    public void update(SysMenuDto dto) {
        SysMenu menu = getById(dto.getId());
        if (menu == null) throw new BizException("菜单不存在");
        copyDtoToEntity(dto, menu);
        menu.setUpdateBy(SecurityUtil.getUserId());
        updateById(menu);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        // 检查是否有子菜单
        for (Long id : ids) {
            long childCount = count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
            if (childCount > 0) throw new BizException("存在子菜单，无法删除");
        }
        removeByIds(ids);
        // 删除角色菜单关联
        for (Long id : ids) {
            roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id));
        }
    }

    @Override
    public List<MenuOptionVO> getMenuOptions() {
        List<SysMenu> menus = list(new LambdaQueryWrapper<SysMenu>()
                .in(SysMenu::getMenuType, 1, 2)
                .orderByAsc(SysMenu::getSort));
        List<MenuOptionVO> voList = menus.stream().map(m -> {
            MenuOptionVO vo = new MenuOptionVO();
            vo.setId(m.getId().toString());
            vo.setTitle(m.getTitle());
            return vo;
        }).collect(Collectors.toList());
        return buildOptionTree(voList, menus);
    }

    private void copyDtoToEntity(SysMenuDto dto, SysMenu menu) {
        menu.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        if (dto.getTitle() != null) menu.setTitle(dto.getTitle());
        if (dto.getName() != null) menu.setName(dto.getName());
        if (dto.getPath() != null) menu.setPath(dto.getPath());
        if (dto.getComponent() != null) menu.setComponent(dto.getComponent());
        if (dto.getRedirect() != null) menu.setRedirect(dto.getRedirect());
        if (dto.getIcon() != null) menu.setIcon(dto.getIcon());
        if (dto.getMenuType() != null) menu.setMenuType(dto.getMenuType());
        if (dto.getPermission() != null) menu.setPermission(dto.getPermission());
        if (dto.getSort() != null) menu.setSort(dto.getSort());
        if (dto.getVisible() != null) menu.setVisible(dto.getVisible());
        if (dto.getKeepAlive() != null) menu.setKeepAlive(dto.getKeepAlive());
        if (dto.getActiveMenu() != null) menu.setActiveMenu(dto.getActiveMenu());
        if (dto.getBreadcrumb() != null) menu.setBreadcrumb(dto.getBreadcrumb());
        if (dto.getShowInTabs() != null) menu.setShowInTabs(dto.getShowInTabs());
        if (dto.getAffix() != null) menu.setAffix(dto.getAffix());
        if (dto.getAlwaysShow() != null) menu.setAlwaysShow(dto.getAlwaysShow());
        if (dto.getStatus() != null) menu.setStatus(dto.getStatus());
    }

    private SysMenuVO toVO(SysMenu menu) {
        SysMenuVO vo = new SysMenuVO();
        vo.setId(menu.getId().toString());
        vo.setParentId(menu.getParentId() != null ? menu.getParentId().toString() : "0");
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setRedirect(menu.getRedirect());
        vo.setType(menu.getMenuType());
        vo.setTitle(menu.getTitle());
        vo.setName(menu.getName());
        vo.setIcon(menu.getIcon());
        vo.setKeepAlive(menu.getKeepAlive() != null && menu.getKeepAlive() == 1);
        vo.setHidden(menu.getVisible() != null && menu.getVisible() == 0);
        vo.setSort(menu.getSort());
        vo.setActiveMenu(menu.getActiveMenu());
        vo.setBreadcrumb(menu.getBreadcrumb() == null || menu.getBreadcrumb() == 1);
        vo.setStatus(menu.getStatus());
        vo.setPermission(menu.getPermission());
        vo.setShowInTabs(menu.getShowInTabs() == null || menu.getShowInTabs() == 1);
        vo.setAffix(menu.getAffix() != null && menu.getAffix() == 1);
        vo.setAlwaysShow(menu.getAlwaysShow() != null && menu.getAlwaysShow() == 1);
        vo.setCreateTime(menu.getCreateTime());

        // 获取拥有该菜单的角色编码列表
        vo.setRoles(new ArrayList<>());
        return vo;
    }

    private List<SysMenuVO> buildTree(List<SysMenuVO> voList) {
        Map<String, SysMenuVO> voMap = new LinkedHashMap<>();
        for (SysMenuVO vo : voList) {
            voMap.put(vo.getId(), vo);
        }

        List<SysMenuVO> tree = new ArrayList<>();
        for (SysMenuVO vo : voList) {
            String parentId = vo.getParentId();
            if (parentId == null || "0".equals(parentId) || parentId.isBlank()) {
                tree.add(vo);
            } else {
                SysMenuVO parent = voMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(vo);
                } else {
                    tree.add(vo);
                }
            }
        }
        return tree;
    }

    private List<MenuOptionVO> buildOptionTree(List<MenuOptionVO> voList, List<SysMenu> menus) {
        Map<Long, MenuOptionVO> voMap = new LinkedHashMap<>();
        for (int i = 0; i < menus.size(); i++) {
            voMap.put(menus.get(i).getId(), voList.get(i));
        }

        List<MenuOptionVO> tree = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            SysMenu menu = menus.get(i);
            MenuOptionVO vo = voList.get(i);
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                tree.add(vo);
            } else {
                MenuOptionVO parent = voMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(vo);
                } else {
                    tree.add(vo);
                }
            }
        }
        return tree;
    }
}
