package com.miujoke.fundadmin.controller;

import com.miujoke.fundadmin.common.Result;
import com.miujoke.fundadmin.dto.LoginDTO;
import com.miujoke.fundadmin.service.AuthService;
import com.miujoke.fundadmin.vo.CaptchaVO;
import com.miujoke.fundadmin.vo.LoginVO;
import com.miujoke.fundadmin.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public Result<CaptchaVO> getCaptcha() {
        return Result.success(authService.generateCaptcha());
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<UserInfoVO> getCurrentUser() {
        Long userId = getCurrentUserId();
        return Result.success(authService.getCurrentUser(userId));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        Long userId = getCurrentUserId();
        authService.logout(userId);
        return Result.success();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            return (Long) authentication.getPrincipal();
        }
        throw new RuntimeException("无法获取当前用户ID");
    }
}