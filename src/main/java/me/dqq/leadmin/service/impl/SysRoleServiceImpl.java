package me.dqq.leadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.common.exception.BizException;
import me.dqq.leadmin.dto.SysRoleDto;
import me.dqq.leadmin.entity.SysRole;
import me.dqq.leadmin.entity.SysRoleMenu;
import me.dqq.leadmin.entity.SysUser;
import me.dqq.leadmin.mapper.SysRoleMapper;
import me.dqq.leadmin.mapper.SysRoleMenuMapper;
import me.dqq.leadmin.mapper.SysUserMapper;
import me.dqq.leadmin.service.SysRoleService;
import me.dqq.leadmin.util.SecurityUtil;
import me.dqq.leadmin.vo.SysRoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<SysRoleVO> getList(Integer page, Integer size, String name, Integer status) {
        int pageNum = page != null ? page : 1;
        int pageSize = size != null ? size : 10;

        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<SysRole>()
                .like(name != null && !name.isBlank(), SysRole::getRoleName, name)
                .eq(status != null, SysRole::getStatus, status)
                .orderByAsc(SysRole::getSort);

        Page<SysRole> pageResult = page(new Page<>(pageNum, pageSize), wrapper);

        List<SysRoleVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public SysRoleVO getDetail(Long id) {
        SysRole role = getById(id);
        if (role == null) throw new BizException("角色不存在");
        return toVO(role);
    }

    @Override
    @Transactional
    public void add(SysRoleDto dto) {
        long count = count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, dto.getRoleCode()));
        if (count > 0) throw new BizException("角色编码已存在");

        SysRole role = new SysRole();
        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        role.setSort(dto.getSort() != null ? dto.getSort() : 0);
        role.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        role.setRemark(dto.getRemark());
        role.setCreateBy(SecurityUtil.getUserId());
        role.setUpdateBy(SecurityUtil.getUserId());
        save(role);

        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            setRoleMenus(role.getId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional
    public void update(SysRoleDto dto) {
        SysRole role = getById(dto.getId());
        if (role == null) throw new BizException("角色不存在");

        role.setRoleName(dto.getRoleName());
        role.setRoleCode(dto.getRoleCode());
        if (dto.getSort() != null) role.setSort(dto.getSort());
        if (dto.getStatus() != null) role.setStatus(dto.getStatus());
        if (dto.getRemark() != null) role.setRemark(dto.getRemark());
        role.setUpdateBy(SecurityUtil.getUserId());
        updateById(role);

        if (dto.getMenuIds() != null) {
            setRoleMenus(dto.getId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        removeByIds(ids);
        for (Long id : ids) {
            roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, id));
        }
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        return roleMenuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    public void setRoleMenus(Long roleId, List<Long> menuIds) {
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Long menuId : menuIds) {
                roleMenuMapper.insert(new SysRoleMenu(roleId, menuId));
            }
        }
    }

    private SysRoleVO toVO(SysRole role) {
        SysRoleVO vo = new SysRoleVO();
        vo.setId(role.getId().toString());
        vo.setCreateTime(role.getCreateTime());
        vo.setDisabled(role.getStatus() == 0);
        vo.setName(role.getRoleName());
        vo.setCode(role.getRoleCode());
        vo.setSort(role.getSort());
        vo.setStatus(role.getStatus());
        vo.setType(1);
        vo.setDescription(role.getRemark());

        if (role.getCreateBy() != null) {
            SysUser creator = userMapper.selectById(role.getCreateBy());
            vo.setCreateUserString(creator != null ? creator.getNickname() : "");
        }
        return vo;
    }
}
