package com.miujoke.fundadmin.service;

import com.miujoke.fundadmin.entity.SysUser;

public interface SysUserService {

    SysUser getByUsername(String username);

    SysUser getById(Long id);

    boolean updateLastLoginTime(Long userId);
}