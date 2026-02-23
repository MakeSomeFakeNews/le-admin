package me.dqq.leadmin.controller;

import me.dqq.leadmin.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/success")
    public R<String> success() {
        return R.ok("哈哈哈");
    }

    @GetMapping("/fail")
    public R<Void> fail() {
        return R.fail(400, "测试失败接口");
    }
}
