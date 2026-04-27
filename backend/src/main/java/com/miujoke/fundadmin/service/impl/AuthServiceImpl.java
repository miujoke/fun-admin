package com.miujoke.fundadmin.service.impl;

import cn.hutool.core.util.IdUtil;
import com.miujoke.fundadmin.common.ResultCode;
import com.miujoke.fundadmin.dto.LoginDTO;
import com.miujoke.fundadmin.entity.SysUser;
import com.miujoke.fundadmin.exception.BusinessException;
import com.miujoke.fundadmin.security.JwtTokenProvider;
import com.miujoke.fundadmin.service.AuthService;
import com.miujoke.fundadmin.service.SysUserService;
import com.miujoke.fundadmin.util.CaptchaUtils;
import com.miujoke.fundadmin.vo.CaptchaVO;
import com.miujoke.fundadmin.vo.LoginVO;
import com.miujoke.fundadmin.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserService sysUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Value("${captcha.width:120}")
    private int captchaWidth;

    @Value("${captcha.height:40}")
    private int captchaHeight;

    @Value("${captcha.code-length:4}")
    private int captchaCodeLength;

    @Value("${captcha.expiration:300}")
    private int captchaExpiration;

    // 内存存储验证码(生产环境应替换为Redis)
    private final Map<String, CaptchaEntry> captchaStore = new ConcurrentHashMap<>();

    @Override
    public CaptchaVO generateCaptcha() {
        String captchaKey = IdUtil.simpleUUID();
        CaptchaUtils.CaptchaResult result = CaptchaUtils.generateCaptcha(captchaWidth, captchaHeight, captchaCodeLength);

        // 存储验证码
        captchaStore.put(captchaKey, new CaptchaEntry(result.code(), System.currentTimeMillis() + captchaExpiration * 1000L));

        // 转图片为Base64
        String base64Image;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(result.image(), "png", os);
            base64Image = "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) {
            log.error("验证码图片转换失败", e);
            throw new BusinessException("验证码生成失败");
        }

        // 清理过期验证码
        cleanExpiredCaptcha();

        return new CaptchaVO(captchaKey, base64Image);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 验证验证码
        CaptchaEntry entry = captchaStore.get(loginDTO.getCaptchaKey());
        if (entry == null || entry.expireTime < System.currentTimeMillis()) {
            captchaStore.remove(loginDTO.getCaptchaKey());
            throw new BusinessException(ResultCode.CAPTCHA_EXPIRED);
        }
        if (!entry.code.equalsIgnoreCase(loginDTO.getCaptchaCode())) {
            captchaStore.remove(loginDTO.getCaptchaKey());
            throw new BusinessException(ResultCode.CAPTCHA_ERROR);
        }
        captchaStore.remove(loginDTO.getCaptchaKey());

        // 验证用户
        SysUser user = sysUserService.getByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 生成Token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        // 更新登录时间
        sysUserService.updateLastLoginTime(user.getId());

        // 构建返回
        UserInfoVO userInfo = toUserInfoVO(user);
        return new LoginVO(token, userInfo);
    }

    @Override
    public UserInfoVO getCurrentUser(Long userId) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return toUserInfoVO(user);
    }

    @Override
    public void logout(Long userId) {
        // JWT无状态，客户端删除Token即可；如有Redis可在此处将Token加入黑名单
        log.info("用户 {} 退出登录", userId);
    }

    private UserInfoVO toUserInfoVO(SysUser user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setStatus(user.getStatus());
        return vo;
    }

    private void cleanExpiredCaptcha() {
        long now = System.currentTimeMillis();
        captchaStore.entrySet().removeIf(e -> e.getValue().expireTime < now);
    }

    private record CaptchaEntry(String code, long expireTime) {}
}