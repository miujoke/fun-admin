package com.miujoke.fundadmin.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 认证相关 1xxx
    UNAUTHORIZED(1001, "未登录或Token已过期"),
    ACCESS_DENIED(1002, "没有访问权限"),
    TOKEN_INVALID(1003, "Token无效"),
    CAPTCHA_EXPIRED(1004, "验证码已过期"),
    CAPTCHA_ERROR(1005, "验证码错误"),
    USER_NOT_FOUND(1006, "用户不存在"),
    PASSWORD_ERROR(1007, "密码错误"),
    USER_DISABLED(1008, "用户已被禁用"),
    USER_ALREADY_EXISTS(1009, "用户名已存在"),

    // 参数校验 2xxx
    PARAM_ERROR(2001, "参数校验失败"),
    PARAM_MISSING(2002, "缺少必要参数"),

    // 业务相关 3xxx
    DATA_NOT_FOUND(3001, "数据不存在"),
    DATA_ALREADY_EXISTS(3002, "数据已存在"),
    OPERATION_NOT_ALLOWED(3003, "操作不允许");

    private final int code;
    private final String msg;
}