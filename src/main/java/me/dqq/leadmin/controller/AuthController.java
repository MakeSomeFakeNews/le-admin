package me.dqq.leadmin.controller;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dqq.leadmin.common.R;
import me.dqq.leadmin.dto.LoginDto;
import me.dqq.leadmin.service.AuthService;
import me.dqq.leadmin.util.IpUtil;
import me.dqq.leadmin.vo.RouteVO;
import me.dqq.leadmin.vo.UserInfoVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public R<Map<String, String>> login(@Valid @RequestBody LoginDto dto, HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        String userAgentStr = request.getHeader("User-Agent");
        String browser = "未知";
        String os = "未知";
        try {
            UserAgent ua = UserAgentUtil.parse(userAgentStr);
            browser = ua.getBrowser().getName();
            os = ua.getOs().getName();
        } catch (Exception ignored) {}
        return R.ok(authService.login(dto, ip, browser, os));
    }

    @PostMapping("/logout")
    public R<Void> logout() {
        authService.logout();
        return R.ok();
    }

    @GetMapping("/getUserInfo")
    public R<UserInfoVO> getUserInfo() {
        return R.ok(authService.getUserInfo());
    }

    @GetMapping("/getUserRoutes")
    public R<List<RouteVO>> getUserRoutes() {
        return R.ok(authService.getUserRoutes());
    }
}
