package me.dqq.leadmin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.annotation.Log;
import me.dqq.leadmin.common.PageResult;
import me.dqq.leadmin.common.R;
import me.dqq.leadmin.dto.DeleteDto;
import me.dqq.leadmin.dto.SysUserDto;
import me.dqq.leadmin.service.SysUserService;
import me.dqq.leadmin.vo.SysUserVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @GetMapping("/getList")
    public R<PageResult<SysUserVO>> getList(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer status) {
        return R.ok(userService.getList(page, size, username, nickname, status));
    }

    @GetMapping("/getDetail")
    public R<SysUserVO> getDetail(@RequestParam Long id) {
        return R.ok(userService.getDetail(id));
    }

    @PostMapping("/add")
    @Log(module = "用户管理", action = "新增用户")
    public R<Void> add(@Valid @RequestBody SysUserDto dto) {
        userService.add(dto);
        return R.ok();
    }

    @PostMapping("/update")
    @Log(module = "用户管理", action = "修改用户")
    public R<Void> update(@RequestBody SysUserDto dto) {
        userService.update(dto);
        return R.ok();
    }

    @PostMapping("/delete")
    @Log(module = "用户管理", action = "删除用户")
    public R<Void> delete(@RequestBody DeleteDto dto) {
        userService.delete(dto.getIds());
        return R.ok();
    }
}
