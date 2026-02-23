package me.dqq.leadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.dqq.leadmin.dto.SysDeptDto;
import me.dqq.leadmin.entity.SysDept;
import me.dqq.leadmin.vo.SysDeptVO;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    List<SysDeptVO> getList(String name, Integer status);
    SysDeptVO getDetail(Long id);
    void add(SysDeptDto dto);
    void update(SysDeptDto dto);
    void delete(List<Long> ids);
}
