package me.dqq.leadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.common.exception.BizException;
import me.dqq.leadmin.dto.SysDeptDto;
import me.dqq.leadmin.entity.SysDept;
import me.dqq.leadmin.mapper.SysDeptMapper;
import me.dqq.leadmin.service.SysDeptService;
import me.dqq.leadmin.util.SecurityUtil;
import me.dqq.leadmin.vo.SysDeptVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Override
    public List<SysDeptVO> getList(String name, Integer status) {
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<SysDept>()
                .like(name != null && !name.isBlank(), SysDept::getName, name)
                .eq(status != null, SysDept::getStatus, status)
                .orderByAsc(SysDept::getSort);

        List<SysDept> depts = list(wrapper);
        List<SysDeptVO> voList = depts.stream().map(this::toVO).collect(Collectors.toList());
        return buildTree(voList, depts);
    }

    @Override
    public SysDeptVO getDetail(Long id) {
        SysDept dept = getById(id);
        if (dept == null) throw new BizException("部门不存在");
        return toVO(dept);
    }

    @Override
    @Transactional
    public void add(SysDeptDto dto) {
        SysDept dept = new SysDept();
        copyDtoToEntity(dto, dept);
        dept.setCreateBy(SecurityUtil.getUserId());
        dept.setUpdateBy(SecurityUtil.getUserId());
        save(dept);
    }

    @Override
    @Transactional
    public void update(SysDeptDto dto) {
        SysDept dept = getById(dto.getId());
        if (dept == null) throw new BizException("部门不存在");
        copyDtoToEntity(dto, dept);
        dept.setUpdateBy(SecurityUtil.getUserId());
        updateById(dept);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (Long id : ids) {
            long childCount = count(new LambdaQueryWrapper<SysDept>().eq(SysDept::getParentId, id));
            if (childCount > 0) throw new BizException("存在子部门，无法删除");
        }
        removeByIds(ids);
    }

    private void copyDtoToEntity(SysDeptDto dto, SysDept dept) {
        dept.setParentId(dto.getParentId() != null ? dto.getParentId() : 0L);
        if (dto.getName() != null) dept.setName(dto.getName());
        if (dto.getSort() != null) dept.setSort(dto.getSort());
        if (dto.getStatus() != null) dept.setStatus(dto.getStatus());
        if (dto.getDescription() != null) dept.setDescription(dto.getDescription());
    }

    private SysDeptVO toVO(SysDept dept) {
        SysDeptVO vo = new SysDeptVO();
        vo.setId(dept.getId().toString());
        vo.setParentId(dept.getParentId() != null ? dept.getParentId().toString() : "0");
        vo.setName(dept.getName());
        vo.setSort(dept.getSort());
        vo.setStatus(dept.getStatus());
        vo.setCreateTime(dept.getCreateTime());
        vo.setDescription(dept.getDescription());
        return vo;
    }

    private List<SysDeptVO> buildTree(List<SysDeptVO> voList, List<SysDept> depts) {
        Map<Long, SysDeptVO> voMap = new LinkedHashMap<>();
        for (int i = 0; i < depts.size(); i++) {
            voMap.put(depts.get(i).getId(), voList.get(i));
        }

        List<SysDeptVO> tree = new ArrayList<>();
        for (int i = 0; i < depts.size(); i++) {
            SysDept dept = depts.get(i);
            SysDeptVO vo = voList.get(i);
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                tree.add(vo);
            } else {
                SysDeptVO parent = voMap.get(dept.getParentId());
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
