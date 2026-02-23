package me.dqq.leadmin.util;

import cn.dev33.satoken.stp.StpUtil;

public class SecurityUtil {

    public static Long getUserId() {
        Object loginId = StpUtil.getLoginId(null);
        if (loginId == null) return null;
        return Long.parseLong(loginId.toString());
    }

    public static Long getUserIdOrThrow() {
        return Long.parseLong(StpUtil.getLoginId().toString());
    }
}
