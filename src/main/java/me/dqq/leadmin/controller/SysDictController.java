package me.dqq.leadmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.annotation.Log;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.common.R;
import me.dqq.leadmin.dto.DeleteDto;
import me.dqq.leadmin.dto.SysDictDataDto;
import me.dqq.leadmin.dto.SysDictDto;
import me.dqq.leadmin.service.SysDictService;
import me.dqq.leadmin.vo.SysDictDataVO;
import me.dqq.leadmin.vo.SysDictVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictService dictService;

    @GetMapping("/getList")
    public R<PageResult<SysDictVO>> getList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer status) {
        return R.ok(dictService.getList(page, size, name, code, status));
    }

    @GetMapping("/getDetail")
    public R<SysDictVO> getDetail(@RequestParam Long id) {
        return R.ok(dictService.getDetail(id));
    }

    @PostMapping("/add")
    @Log(module = "字典管理", action = "新增字典")
    public R<Void> add(@Valid @RequestBody SysDictDto dto) {
        dictService.add(dto);
        return R.ok();
    }

    @PostMapping("/update")
    @Log(module = "字典管理", action = "修改字典")
    public R<Void> update(@RequestBody SysDictDto dto) {
        dictService.update(dto);
        return R.ok();
    }

    @PostMapping("/delete")
    @Log(module = "字典管理", action = "删除字典")
    public R<Void> delete(@RequestBody DeleteDto dto) {
        dictService.delete(dto.getIds());
        return R.ok();
    }

    @GetMapping("/getDictDataList")
    public R<PageResult<SysDictDataVO>> getDictDataList(
            @RequestParam String code,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        return R.ok(dictService.getDictDataList(code, page, size));
    }

    @GetMapping("/getDictDataDetail")
    public R<SysDictDataVO> getDictDataDetail(
            @RequestParam Long id,
            @RequestParam(required = false) String code) {
        return R.ok(dictService.getDictDataDetail(id, code));
    }

    @GetMapping("/getDictData")
    public R<Map<String, List<SysDictDataVO>>> getDictData() {
        return R.ok(dictService.getDictData());
    }

    @PostMapping("/addDictData")
    @Log(module = "字典管理", action = "新增字典数据")
    public R<Void> addDictData(@Valid @RequestBody SysDictDataDto dto) {
        dictService.addDictData(dto);
        return R.ok();
    }

    @PostMapping("/updateDictData")
    @Log(module = "字典管理", action = "修改字典数据")
    public R<Void> updateDictData(@RequestBody SysDictDataDto dto) {
        dictService.updateDictData(dto);
        return R.ok();
    }

    @PostMapping("/deleteDictData")
    @Log(module = "字典管理", action = "删除字典数据")
    public R<Void> deleteDictData(@RequestBody DeleteDto dto) {
        dictService.deleteDictData(dto.getIds());
        return R.ok();
    }
}
