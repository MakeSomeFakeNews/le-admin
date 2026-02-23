package me.dqq.leadmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.annotation.Log;
import me.dqq.leadmin.common.R;
import me.dqq.leadmin.dto.DeleteDto;
import me.dqq.leadmin.dto.SysMenuDto;
import me.dqq.leadmin.service.SysMenuService;
import me.dqq.leadmin.vo.MenuOptionVO;
import me.dqq.leadmin.vo.SysMenuVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    @GetMapping("/getList")
    public R<List<SysMenuVO>> getList() {
        return R.ok(menuService.getList());
    }

    @GetMapping("/getDetail")
    public R<SysMenuVO> getDetail(@RequestParam Long id) {
        return R.ok(menuService.getDetail(id));
    }

    @PostMapping("/add")
    @Log(module = "菜单管理", action = "新增菜单")
    public R<Void> add(@Valid @RequestBody SysMenuDto dto) {
        menuService.add(dto);
        return R.ok();
    }

    @PostMapping("/update")
    @Log(module = "菜单管理", action = "修改菜单")
    public R<Void> update(@RequestBody SysMenuDto dto) {
        menuService.update(dto);
        return R.ok();
    }

    @PostMapping("/delete")
    @Log(module = "菜单管理", action = "删除菜单")
    public R<Void> delete(@RequestBody DeleteDto dto) {
        menuService.delete(dto.getIds());
        return R.ok();
    }

    @GetMapping("/getMenuOptions")
    public R<List<MenuOptionVO>> getMenuOptions() {
        return R.ok(menuService.getMenuOptions());
    }
}
