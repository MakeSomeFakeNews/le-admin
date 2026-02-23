package me.dqq.leadmin.config;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.dqq.leadmin.entity.SysUser;
import me.dqq.leadmin.mapper.SysUserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化默认用户密码，修正 schema.sql 中可能不正确的 BCrypt 哈希值
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper userMapper;

    @Override
    public void run(String... args) {
        ensurePassword("admin", "123456");
        ensurePassword("user", "123456");
    }

    private void ensurePassword(String username, String rawPassword) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username));
        if (user == null) return;

        boolean valid = false;
        try {
            valid = BCrypt.checkpw(rawPassword, user.getPassword());
        } catch (Exception ignored) {}

        if (!valid) {
            user.setPassword(BCrypt.hashpw(rawPassword));
            userMapper.updateById(user);
            log.info("[DataInit] 已修正用户 {} 的默认密码哈希", username);
        }
    }
}
