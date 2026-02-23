package me.dqq.leadmin.service;

import me.dqq.leadmin.dto.LoginDto;
import me.dqq.leadmin.vo.RouteVO;
import me.dqq.leadmin.vo.UserInfoVO;

import java.util.List;
import java.util.Map;

public interface AuthService {
    Map<String, String> login(LoginDto dto, String ip, String browser, String os);
    void logout();
    UserInfoVO getUserInfo();
    List<RouteVO> getUserRoutes();
}
