-- 基金管理系统 数据库初始化脚本
-- MySQL 8.x

CREATE DATABASE IF NOT EXISTS fund_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE fund_admin;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY COMMENT '主键ID(雪花算法)',
    username VARCHAR(64) NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname VARCHAR(64) NULL COMMENT '昵称',
    avatar VARCHAR(255) NULL COMMENT '头像URL',
    email VARCHAR(128) NULL COMMENT '邮箱',
    phone VARCHAR(32) NULL COMMENT '手机号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态(1正常 0禁用)',
    last_login_time DATETIME NULL COMMENT '最后登录时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT NULL COMMENT '创建人ID',
    update_by BIGINT NULL COMMENT '更新人ID',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0未删除 1已删除)',
    remark VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 用户名唯一索引
CREATE UNIQUE INDEX uk_sys_user_username ON sys_user(username) WHERE deleted = 0;

-- 角色表(后续扩展)
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    role_name VARCHAR(64) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(64) NOT NULL COMMENT '角色编码',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态(1正常 0禁用)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT NULL COMMENT '创建人ID',
    update_by BIGINT NULL COMMENT '更新人ID',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    remark VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

CREATE UNIQUE INDEX uk_sys_role_code ON sys_role(role_code) WHERE deleted = 0;

-- 菜单表(后续扩展)
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(64) NOT NULL COMMENT '菜单名称',
    menu_type TINYINT NOT NULL COMMENT '类型(1目录 2菜单 3按钮)',
    path VARCHAR(255) NULL COMMENT '路由路径',
    component VARCHAR(255) NULL COMMENT '组件路径',
    permission VARCHAR(128) NULL COMMENT '权限标识',
    icon VARCHAR(64) NULL COMMENT '图标',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序',
    visible TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见(1可见 0隐藏)',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态(1正常 0禁用)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT NULL COMMENT '创建人ID',
    update_by BIGINT NULL COMMENT '更新人ID',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    remark VARCHAR(500) NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 用户角色关联表(后续扩展)
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

CREATE INDEX idx_sys_user_role_user ON sys_user_role(user_id);
CREATE INDEX idx_sys_user_role_role ON sys_user_role(role_id);

-- 角色菜单关联表(后续扩展)
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT PRIMARY KEY COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

CREATE INDEX idx_sys_role_menu_role ON sys_role_menu(role_id);