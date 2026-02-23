package me.dqq.leadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.common.exception.BizException;
import me.dqq.leadmin.dto.SysDictDataDto;
import me.dqq.leadmin.dto.SysDictDto;
import me.dqq.leadmin.entity.SysDict;
import me.dqq.leadmin.entity.SysDictData;
import me.dqq.leadmin.entity.SysUser;
import me.dqq.leadmin.mapper.SysDictDataMapper;
import me.dqq.leadmin.mapper.SysDictMapper;
import me.dqq.leadmin.mapper.SysUserMapper;
import me.dqq.leadmin.service.SysDictService;
import me.dqq.leadmin.util.SecurityUtil;
import me.dqq.leadmin.vo.SysDictDataVO;
import me.dqq.leadmin.vo.SysDictVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictDataMapper dictDataMapper;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<SysDictVO> getList(Integer page, Integer size, String name, String code, Integer status) {
        int pageNum = page != null ? page : 1;
        int pageSize = size != null ? size : 10;

        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<SysDict>()
                .like(name != null && !name.isBlank(), SysDict::getName, name)
                .like(code != null && !code.isBlank(), SysDict::getCode, code)
                .eq(status != null, SysDict::getStatus, status)
                .orderByAsc(SysDict::getSort);

        Page<SysDict> pageResult = page(new Page<>(pageNum, pageSize), wrapper);
        List<SysDictVO> voList = pageResult.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public SysDictVO getDetail(Long id) {
        SysDict dict = getById(id);
        if (dict == null) throw new BizException("字典不存在");
        SysDictVO vo = toVO(dict);
        // 加载字典数据列表
        List<SysDictData> dataList = dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictId, id)
                .orderByAsc(SysDictData::getSort));
        vo.setList(dataList.stream().map(this::toDataVO).collect(Collectors.toList()));
        return vo;
    }

    @Override
    @Transactional
    public void add(SysDictDto dto) {
        long count = count(new LambdaQueryWrapper<SysDict>().eq(SysDict::getCode, dto.getCode()));
        if (count > 0) throw new BizException("字典编码已存在");

        SysDict dict = new SysDict();
        copyDtoToEntity(dto, dict);
        dict.setCreateBy(SecurityUtil.getUserId());
        dict.setUpdateBy(SecurityUtil.getUserId());
        save(dict);
    }

    @Override
    @Transactional
    public void update(SysDictDto dto) {
        SysDict dict = getById(dto.getId());
        if (dict == null) throw new BizException("字典不存在");
        copyDtoToEntity(dto, dict);
        dict.setUpdateBy(SecurityUtil.getUserId());
        updateById(dict);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        removeByIds(ids);
        // 删除字典数据
        for (Long id : ids) {
            dictDataMapper.delete(new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getDictId, id));
        }
    }

    @Override
    public PageResult<SysDictDataVO> getDictDataList(String code, Integer page, Integer size) {
        int pageNum = page != null ? page : 1;
        int pageSize = size != null ? size : 100;

        Page<SysDictData> pageResult = dictDataMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<SysDictData>()
                        .eq(SysDictData::getDictCode, code)
                        .orderByAsc(SysDictData::getSort));

        List<SysDictDataVO> voList = pageResult.getRecords().stream()
                .map(this::toDataVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, pageResult.getTotal());
    }

    @Override
    public SysDictDataVO getDictDataDetail(Long id, String code) {
        SysDictData data = dictDataMapper.selectById(id);
        if (data == null) throw new BizException("字典数据不存在");
        return toDataVO(data);
    }

    @Override
    public Map<String, List<SysDictDataVO>> getDictData() {
        // 获取所有启用的字典
        List<SysDict> dicts = list(new LambdaQueryWrapper<SysDict>().eq(SysDict::getStatus, 1));
        Map<String, List<SysDictDataVO>> result = new LinkedHashMap<>();

        for (SysDict dict : dicts) {
            List<SysDictData> dataList = dictDataMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                    .eq(SysDictData::getDictCode, dict.getCode())
                    .eq(SysDictData::getStatus, 1)
                    .orderByAsc(SysDictData::getSort));
            result.put(dict.getCode(), dataList.stream().map(this::toDataVO).collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    @Transactional
    public void addDictData(SysDictDataDto dto) {
        SysDictData data = new SysDictData();
        copyDataDtoToEntity(dto, data);
        data.setCreateBy(SecurityUtil.getUserId());
        data.setUpdateBy(SecurityUtil.getUserId());
        dictDataMapper.insert(data);
    }

    @Override
    @Transactional
    public void updateDictData(SysDictDataDto dto) {
        SysDictData data = dictDataMapper.selectById(dto.getId());
        if (data == null) throw new BizException("字典数据不存在");
        copyDataDtoToEntity(dto, data);
        data.setUpdateBy(SecurityUtil.getUserId());
        dictDataMapper.updateById(data);
    }

    @Override
    @Transactional
    public void deleteDictData(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;
        for (Long id : ids) {
            dictDataMapper.deleteById(id);
        }
    }

    private void copyDtoToEntity(SysDictDto dto, SysDict dict) {
        if (dto.getName() != null) dict.setName(dto.getName());
        if (dto.getCode() != null) dict.setCode(dto.getCode());
        if (dto.getSort() != null) dict.setSort(dto.getSort());
        if (dto.getStatus() != null) dict.setStatus(dto.getStatus());
        if (dto.getDescription() != null) dict.setDescription(dto.getDescription());
    }

    private void copyDataDtoToEntity(SysDictDataDto dto, SysDictData data) {
        if (dto.getDictId() != null) data.setDictId(dto.getDictId());
        if (dto.getDictCode() != null) data.setDictCode(dto.getDictCode());
        if (dto.getName() != null) data.setName(dto.getName());
        if (dto.getValue() != null) data.setValue(dto.getValue());
        if (dto.getColor() != null) data.setColor(dto.getColor());
        if (dto.getSort() != null) data.setSort(dto.getSort());
        if (dto.getStatus() != null) data.setStatus(dto.getStatus());
    }

    private SysDictVO toVO(SysDict dict) {
        SysDictVO vo = new SysDictVO();
        vo.setId(dict.getId().toString());
        vo.setCreateTime(dict.getCreateTime());
        vo.setName(dict.getName());
        vo.setCode(dict.getCode());
        vo.setSort(dict.getSort());
        vo.setStatus(dict.getStatus());
        vo.setDescription(dict.getDescription());
        if (dict.getCreateBy() != null) {
            SysUser creator = userMapper.selectById(dict.getCreateBy());
            vo.setCreateUserString(creator != null ? creator.getNickname() : "");
        }
        return vo;
    }

    private SysDictDataVO toDataVO(SysDictData data) {
        SysDictDataVO vo = new SysDictDataVO();
        vo.setId(data.getId().toString());
        vo.setName(data.getName());
        vo.setValue(data.getValue());
        vo.setStatus(data.getStatus());
        vo.setColor(data.getColor());
        vo.setCreateTime(data.getCreateTime());
        return vo;
    }
}
