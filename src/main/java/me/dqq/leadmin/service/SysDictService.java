package me.dqq.leadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.dto.SysDictDataDto;
import me.dqq.leadmin.dto.SysDictDto;
import me.dqq.leadmin.entity.SysDict;
import me.dqq.leadmin.vo.SysDictDataVO;
import me.dqq.leadmin.vo.SysDictVO;

import java.util.List;
import java.util.Map;

public interface SysDictService extends IService<SysDict> {
    PageResult<SysDictVO> getList(Integer page, Integer size, String name, String code, Integer status);
    SysDictVO getDetail(Long id);
    void add(SysDictDto dto);
    void update(SysDictDto dto);
    void delete(List<Long> ids);

    PageResult<SysDictDataVO> getDictDataList(String code, Integer page, Integer size);
    SysDictDataVO getDictDataDetail(Long id, String code);
    Map<String, List<SysDictDataVO>> getDictData();
    void addDictData(SysDictDataDto dto);
    void updateDictData(SysDictDataDto dto);
    void deleteDictData(List<Long> ids);
}
