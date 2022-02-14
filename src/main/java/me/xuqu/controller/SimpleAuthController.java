package me.xuqu.controller;

import me.xuqu.model.support.Result;
import me.xuqu.service.SimpleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限认证控制器（简单实现）
 * - 认证接口
 *
 * @author yec
 * @date 12/6/20 8:04 PM
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SimpleAuthController {

    private final SimpleAuthService simpleAuthService;

    @GetMapping("/auth")
    public Result<String> auth() {
        String s = simpleAuthService.doAuth();
        return Result.ok(s);
    }
}
