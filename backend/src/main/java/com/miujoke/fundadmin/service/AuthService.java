package com.miujoke.fundadmin.service;

import com.miujoke.fundadmin.dto.LoginDTO;
import com.miujoke.fundadmin.vo.CaptchaVO;
import com.miujoke.fundadmin.vo.LoginVO;
import com.miujoke.fundadmin.vo.UserInfoVO;

public interface AuthService {

    CaptchaVO generateCaptcha();

    LoginVO login(LoginDTO loginDTO);

    UserInfoVO getCurrentUser(Long userId);

    void logout(Long userId);
}