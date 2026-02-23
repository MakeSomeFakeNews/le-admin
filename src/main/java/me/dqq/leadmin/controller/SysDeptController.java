package me.dqq.leadmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.annotation.Log;
import me.dqq.leadmin.common.R;
import me.dqq.leadmin.dto.DeleteDto;
import me.dqq.leadmin.dto.SysDeptDto;
import me.dqq.leadmin.service.SysDeptService;
import me.dqq.leadmin.vo.SysDeptVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/dept")
@RequiredArgsConstructor
public class SysDeptController {

    private final SysDeptService deptService;

    @GetMapping("/getList")
    public R<List<SysDeptVO>> getList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        return R.ok(deptService.getList(name, status));
    }

    @GetMapping("/getDetail")
    public R<SysDeptVO> getDetail(@RequestParam Long id) {
        return R.ok(deptService.getDetail(id));
    }

    @PostMapping("/add")
    @Log(module = "部门管理", action = "新增部门")
    public R<Void> add(@Valid @RequestBody SysDeptDto dto) {
        deptService.add(dto);
        return R.ok();
    }

    @PostMapping("/update")
    @Log(module = "部门管理", action = "修改部门")
    public R<Void> update(@RequestBody SysDeptDto dto) {
        deptService.update(dto);
        return R.ok();
    }

    @PostMapping("/delete")
    @Log(module = "部门管理", action = "删除部门")
    public R<Void> delete(@RequestBody DeleteDto dto) {
        deptService.delete(dto.getIds());
        return R.ok();
    }
}
