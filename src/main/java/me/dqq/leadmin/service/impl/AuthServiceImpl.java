package me.dqq.leadmin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dqq.leadmin.common.exception.BizException;
import me.dqq.leadmin.dto.LoginDto;
import me.dqq.leadmin.entity.*;
import me.dqq.leadmin.mapper.*;
import me.dqq.leadmin.service.AuthService;
import me.dqq.leadmin.service.SysLogLoginService;
import me.dqq.leadmin.vo.RouteVO;
import me.dqq.leadmin.vo.UserInfoVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysLogLoginService logLoginService;

    @Override
    public Map<String, String> login(LoginDto dto, String ip, String browser, String os) {
        // 查询用户（@TableLogic 自动过滤已删除记录）
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, dto.getUsername()));

        // 记录登录日志
        SysLogLogin logLogin = new SysLogLogin();
        logLogin.setUsername(dto.getUsername());
        logLogin.setIp(ip);
        logLogin.setBrowser(browser);
        logLogin.setOs(os);
        logLogin.setLoginTime(LocalDateTime.now());

        if (user == null) {
            logLogin.setStatus(0);
            logLogin.setMessage("用户不存在");
            logLoginService.save(logLogin);
            throw new BizException("用户名或密码错误");
        }

        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            logLogin.setStatus(0);
            logLogin.setMessage("密码错误");
            logLoginService.save(logLogin);
            throw new BizException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            logLogin.setStatus(0);
            logLogin.setMessage("账号已禁用");
            logLoginService.save(logLogin);
            throw new BizException("账号已被禁用，请联系管理员");
        }

        // 登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        logLogin.setStatus(1);
        logLogin.setMessage("登录成功");
        logLoginService.save(logLogin);

        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return result;
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public UserInfoVO getUserInfo() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        SysUser user = userMapper.selectById(userId);
        if (user == null) throw new BizException(401, "用户不存在");

        // 获取角色
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        List<String> roleCodes = new ArrayList<>();
        List<String> permissions = new ArrayList<>();

        if (!roleIds.isEmpty()) {
            List<SysRole> roles = roleMapper.selectBatchIds(roleIds);
            roleCodes = roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());

            // 超级管理员拥有所有权限
            boolean isAdmin = roleCodes.contains("role_admin");
            if (isAdmin) {
                permissions = List.of("*:*:*");
            } else {
                // 获取权限列表
                String roleIdsStr = roleIds.stream().map(Object::toString).collect(Collectors.joining(","));
                List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIdsStr);
                if (!menuIds.isEmpty()) {
                    List<SysMenu> menus = menuMapper.selectBatchIds(menuIds);
                    permissions = menus.stream()
                            .filter(m -> m.getPermission() != null && !m.getPermission().isBlank())
                            .map(SysMenu::getPermission)
                            .distinct()
                            .collect(Collectors.toList());
                }
            }
        }

        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname() != null ? user.getNickname() : user.getUsername());
        vo.setAvatar(user.getAvatar());
        vo.setRoles(roleCodes);
        vo.setPermissions(permissions);
        return vo;
    }

    @Override
    public List<RouteVO> getUserRoutes() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());

        // 获取用户角色
        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(userId);
        List<SysRole> roles = roleIds.isEmpty() ? new ArrayList<>() : roleMapper.selectBatchIds(roleIds);
        List<String> roleCodes = roles.stream().map(SysRole::getRoleCode).collect(Collectors.toList());

        // 查询所有有效菜单（type=1目录 或 type=2菜单，@TableLogic 自动过滤已删除记录）
        List<SysMenu> allMenus;
        boolean isAdmin = roleCodes.contains("role_admin");

        if (isAdmin) {
            // 超级管理员获取所有菜单
            allMenus = menuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .in(SysMenu::getMenuType, 1, 2)
                    .eq(SysMenu::getStatus, 1)
                    .orderByAsc(SysMenu::getSort));
        } else {
            // 普通用户获取角色关联的菜单
            if (roleIds.isEmpty()) return new ArrayList<>();
            String roleIdsStr = roleIds.stream().map(Object::toString).collect(Collectors.joining(","));
            List<Long> menuIds = roleMenuMapper.selectMenuIdsByRoleIds(roleIdsStr);
            if (menuIds.isEmpty()) return new ArrayList<>();

            allMenus = menuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                    .in(SysMenu::getId, menuIds)
                    .in(SysMenu::getMenuType, 1, 2)
                    .eq(SysMenu::getStatus, 1)
                    .orderByAsc(SysMenu::getSort));
        }

        // 将菜单转为 RouteVO 并构建树
        Map<Long, RouteVO> voMap = new LinkedHashMap<>();
        for (SysMenu menu : allMenus) {
            RouteVO vo = menuToRouteVO(menu, roleCodes);
            voMap.put(menu.getId(), vo);
        }

        // 构建树形结构
        List<RouteVO> tree = new ArrayList<>();
        for (SysMenu menu : allMenus) {
            RouteVO vo = voMap.get(menu.getId());
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                tree.add(vo);
            } else {
                RouteVO parent = voMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) parent.setChildren(new ArrayList<>());
                    parent.getChildren().add(vo);
                } else {
                    // 父节点不在当前用户菜单中，作为根节点
                    tree.add(vo);
                }
            }
        }
        return tree;
    }

    private RouteVO menuToRouteVO(SysMenu menu, List<String> userRoles) {
        RouteVO vo = new RouteVO();
        vo.setId(menu.getId().toString());
        vo.setParentId(menu.getParentId() != null ? menu.getParentId().toString() : "");
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setRedirect(menu.getRedirect());
        vo.setType(menu.getMenuType());
        vo.setTitle(menu.getTitle());
        vo.setIcon(menu.getIcon());
        vo.setKeepAlive(menu.getKeepAlive() != null && menu.getKeepAlive() == 1);
        vo.setHidden(menu.getVisible() != null && menu.getVisible() == 0); // visible=0 表示隐藏
        vo.setSort(menu.getSort());
        vo.setActiveMenu(menu.getActiveMenu());
        vo.setBreadcrumb(menu.getBreadcrumb() == null || menu.getBreadcrumb() == 1);
        vo.setStatus(menu.getStatus());
        vo.setRoles(userRoles);
        vo.setPermission(menu.getPermission());
        vo.setShowInTabs(menu.getShowInTabs() == null || menu.getShowInTabs() == 1);
        vo.setAffix(menu.getAffix() != null && menu.getAffix() == 1);
        vo.setAlwaysShow(menu.getAlwaysShow() != null && menu.getAlwaysShow() == 1);
        return vo;
    }
}
