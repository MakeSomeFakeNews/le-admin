package me.dqq.leadmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.annotation.Log;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.common.R;
import me.dqq.leadmin.dto.DeleteDto;
import me.dqq.leadmin.dto.SysRoleDto;
import me.dqq.leadmin.service.SysRoleService;
import me.dqq.leadmin.vo.SysRoleVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    @GetMapping("/getList")
    public R<PageResult<SysRoleVO>> getList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        return R.ok(roleService.getList(page, size, name, status));
    }

    @GetMapping("/getDetail")
    public R<SysRoleVO> getDetail(@RequestParam Long id) {
        return R.ok(roleService.getDetail(id));
    }

    @PostMapping("/add")
    @Log(module = "角色管理", action = "新增角色")
    public R<Void> add(@Valid @RequestBody SysRoleDto dto) {
        roleService.add(dto);
        return R.ok();
    }

    @PostMapping("/update")
    @Log(module = "角色管理", action = "修改角色")
    public R<Void> update(@RequestBody SysRoleDto dto) {
        roleService.update(dto);
        return R.ok();
    }

    @PostMapping("/delete")
    @Log(module = "角色管理", action = "删除角色")
    public R<Void> delete(@RequestBody DeleteDto dto) {
        roleService.delete(dto.getIds());
        return R.ok();
    }

    @GetMapping("/getRoleMenuIds")
    public R<List<Long>> getRoleMenuIds(@RequestParam Long roleId) {
        return R.ok(roleService.getRoleMenuIds(roleId));
    }
}
