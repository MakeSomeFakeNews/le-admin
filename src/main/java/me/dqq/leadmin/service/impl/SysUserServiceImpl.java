package me.dqq.leadmin.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.common.exception.BizException;
import me.dqq.leadmin.dto.SysUserDto;
import me.dqq.leadmin.entity.*;
import me.dqq.leadmin.mapper.*;
import me.dqq.leadmin.service.SysUserService;
import me.dqq.leadmin.util.SecurityUtil;
import me.dqq.leadmin.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final SysDeptMapper deptMapper;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<SysUserVO> getList(Integer page, Integer size, String username, String nickname, Integer status) {
        int pageNum = page != null ? page : 1;
        int pageSize = size != null ? size : 10;

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .like(username != null && !username.isBlank(), SysUser::getUsername, username)
                .like(nickname != null && !nickname.isBlank(), SysUser::getNickname, nickname)
                .eq(status != null, SysUser::getStatus, status)
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> pageResult = page(new Page<>(pageNum, pageSize), wrapper);

        List<SysUserVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public SysUserVO getDetail(Long id) {
        SysUser user = getById(id);
        if (user == null) throw new BizException("用户不存在");
        return toVO(user);
    }

    @Override
    @Transactional
    public void add(SysUserDto dto) {
        long count = count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername()));
        if (count > 0) throw new BizException("用户名已存在");

        SysUser user = new SysUser();
        copyDtoToEntity(dto, user);
        String pwd = dto.getPassword() != null && !dto.getPassword().isBlank()
                ? dto.getPassword() : "123456";
        user.setPassword(BCrypt.hashpw(pwd));
        user.setCreateBy(SecurityUtil.getUserId());
        user.setUpdateBy(SecurityUtil.getUserId());
        save(user);

        saveUserRoles(user.getId(), dto.getRoleIds());
    }

    @Override
    @Transactional
    public void update(SysUserDto dto) {
        SysUser user = getById(dto.getId());
        if (user == null) throw new BizException("用户不存在");

        copyDtoToEntity(dto, user);
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(BCrypt.hashpw(dto.getPassword()));
        }
        user.setUpdateBy(SecurityUtil.getUserId());
        updateById(user);

        if (dto.getRoleIds() != null) {
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, dto.getId()));
            saveUserRoles(dto.getId(), dto.getRoleIds());
        }
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        removeByIds(ids);
        for (Long id : ids) {
            userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, id));
        }
    }

    private void saveUserRoles(Long userId, List<Long> roleIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                userRoleMapper.insert(new SysUserRole(userId, roleId));
            }
        }
    }

    private void copyDtoToEntity(SysUserDto dto, SysUser user) {
        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getDeptId() != null) user.setDeptId(dto.getDeptId());
        if (dto.getUserType() != null) user.setUserType(dto.getUserType());
        if (dto.getDescription() != null) user.setDescription(dto.getDescription());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());
    }

    private SysUserVO toVO(SysUser user) {
        SysUserVO vo = new SysUserVO();
        vo.setId(user.getId().toString());
        vo.setCreateTime(user.getCreateTime());
        vo.setDisabled(user.getStatus() == 0);
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setGender(user.getGender());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setStatus(user.getStatus());
        vo.setType(user.getUserType());
        vo.setDescription(user.getDescription());

        if (user.getDeptId() != null) {
            vo.setDeptId(user.getDeptId().toString());
            SysDept dept = deptMapper.selectById(user.getDeptId());
            if (dept != null) vo.setDeptName(dept.getName());
        }

        List<Long> roleIds = userRoleMapper.selectRoleIdsByUserId(user.getId());
        if (!roleIds.isEmpty()) {
            List<SysRole> roles = roleMapper.selectBatchIds(roleIds);
            vo.setRoleIds(roles.stream().map(r -> r.getId().toString()).collect(Collectors.toList()));
            vo.setRoleNames(roles.stream().map(SysRole::getRoleName).collect(Collectors.joining(",")));
        } else {
            vo.setRoleIds(new ArrayList<>());
            vo.setRoleNames("");
        }

        if (user.getCreateBy() != null) {
            SysUser creator = userMapper.selectById(user.getCreateBy());
            vo.setCreateUserString(creator != null ? creator.getNickname() : "");
        }

        return vo;
    }
}
