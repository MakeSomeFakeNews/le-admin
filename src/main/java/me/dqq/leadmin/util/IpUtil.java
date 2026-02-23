package me.dqq.leadmin.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Slf4j
public class IpUtil {

    private static Searcher searcher;

    static {
        try {
            ClassPathResource resource = new ClassPathResource("ip2region.xdb");
            InputStream inputStream = resource.getInputStream();
            byte[] dbBytes = inputStream.readAllBytes();
            searcher = Searcher.newWithBuffer(dbBytes);
        } catch (Exception e) {
            log.warn("ip2region 数据库加载失败: {}", e.getMessage());
        }
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isBlank(ip)) ip = request.getHeader("Proxy-Client-IP");
        if (isBlank(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
        if (isBlank(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
        if (isBlank(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (isBlank(ip)) ip = request.getRemoteAddr();
        // 多个IP取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    public static String getIpLocation(String ip) {
        if (ip == null || "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return "内网IP";
        }
        if (searcher == null) return "未知";
        try {
            String region = searcher.search(ip);
            // 格式: 国家|区域|省|市|ISP
            if (region != null) {
                String[] parts = region.split("\\|");
                StringBuilder sb = new StringBuilder();
                for (String part : parts) {
                    if (!"0".equals(part) && !part.isEmpty()) {
                        sb.append(part).append(" ");
                    }
                }
                return sb.toString().trim();
            }
        } catch (Exception e) {
            log.warn("IP地址解析失败: {}", e.getMessage());
        }
        return "未知";
    }

    private static boolean isBlank(String str) {
        return str == null || str.isBlank() || "unknown".equalsIgnoreCase(str);
    }
}
