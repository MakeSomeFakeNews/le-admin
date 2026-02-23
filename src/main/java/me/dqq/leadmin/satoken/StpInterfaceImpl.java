package me.dqq.leadmin.satoken;

import cn.dev33.satoken.stp.StpInterface;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.entity.SysMenu;
import me.dqq.leadmin.entity.SysRole;
import me.dqq.leadmin.mapper.SysRoleMapper;
import me.dqq.leadmin.mapper.SysRoleMenuMapper;
import me.dqq.leadmin.mapper.SysUserRoleMapper;
import me.dqq.leadmin.service.SysMenuService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StpInterfaceImpl implements StpInterface {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysMenuService menuService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) return new ArrayList<>();

        // 超级管理员拥有所有权限
        List<SysRole> roles = roleMapper.selectBatchIds(roleIds);
        boolean isAdmin = roles.stream().anyMatch(r -> "role_admin".equals(r.getRoleCode()));
        if (isAdmin) return List.of("*:*:*");

        // 获取角色关联的菜单权限
        String roleIdsStr = roleIds.stream().map(Object::toString).collect(Collectors.joining(","));
        List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIdsStr);
        if (menuIds.isEmpty()) return new ArrayList<>();

        List<SysMenu> menus = menuService.listByIds(menuIds);
        return menus.stream()
                .filter(m -> m.getPermission() != null && !m.getPermission().isBlank())
                .map(SysMenu::getPermission)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Long.parseLong(loginId.toString());
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) return new ArrayList<>();

        List<SysRole> roles = roleMapper.selectBatchIds(roleIds);
        return roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());
    }
}
