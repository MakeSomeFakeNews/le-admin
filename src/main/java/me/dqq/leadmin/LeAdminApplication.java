package me.dqq.leadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("me.dqq.leadmin.mapper")
public class LeAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeAdminApplication.class, args);
    }
}
