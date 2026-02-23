-- 登录日志表（已有）
DROP TABLE IF EXISTS `sys_log_login`;
CREATE TABLE `sys_log_login` (
  `id`           BIGINT NOT NULL AUTO_INCREMENT,
  `username`     VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip`           VARCHAR(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip_location`  VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `browser`      VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `os`           VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status`       TINYINT NOT NULL DEFAULT 1,
  `message`      VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `login_time`   DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 操作日志表（已有）
DROP TABLE IF EXISTS `sys_log_operate`;
CREATE TABLE `sys_log_operate` (
  `id`              BIGINT NOT NULL AUTO_INCREMENT,
  `module`          VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `action`          VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method`          VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_url`     VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_method`  VARCHAR(16)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_params`  TEXT COLLATE utf8mb4_unicode_ci,
  `response_result` TEXT COLLATE utf8mb4_unicode_ci,
  `status`          TINYINT NOT NULL DEFAULT 1,
  `error_msg`       TEXT COLLATE utf8mb4_unicode_ci,
  `operator_id`     BIGINT DEFAULT NULL,
  `operator_name`   VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip`              VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip_location`     VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `duration`        BIGINT DEFAULT NULL,
  `create_time`     DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 菜单表（扩展字段）
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id`           BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id`    BIGINT NOT NULL DEFAULT 0,
  `title`        VARCHAR(64)  COLLATE utf8mb4_unicode_ci NOT NULL,
  `name`         VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `path`         VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `component`    VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `redirect`     VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `icon`         VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `menu_type`    TINYINT NOT NULL DEFAULT 1 COMMENT '1=目录 2=菜单 3=按钮',
  `permission`   VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort`         INT NOT NULL DEFAULT 0,
  `visible`      TINYINT NOT NULL DEFAULT 1 COMMENT '1=显示 0=隐藏',
  `keep_alive`   TINYINT NOT NULL DEFAULT 0,
  `active_menu`  VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `breadcrumb`   TINYINT NOT NULL DEFAULT 1,
  `show_in_tabs` TINYINT NOT NULL DEFAULT 1,
  `affix`        TINYINT NOT NULL DEFAULT 0,
  `always_show`  TINYINT NOT NULL DEFAULT 0,
  `status`       TINYINT NOT NULL DEFAULT 1,
  `deleted`      TINYINT NOT NULL DEFAULT 0,
  `create_by`    BIGINT DEFAULT NULL,
  `update_by`    BIGINT DEFAULT NULL,
  `create_time`  DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_menu_type` (`menu_type`),
  KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 角色表（已有）
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,
  `role_name`   VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_code`   VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort`        INT NOT NULL DEFAULT 0,
  `status`      TINYINT NOT NULL DEFAULT 1,
  `remark`      VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted`     TINYINT NOT NULL DEFAULT 0,
  `create_by`   BIGINT DEFAULT NULL,
  `update_by`   BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 角色菜单关联表（已有）
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 部门表（新增）
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id`   BIGINT NOT NULL DEFAULT 0,
  `name`        VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort`        INT NOT NULL DEFAULT 0,
  `status`      TINYINT NOT NULL DEFAULT 1,
  `description` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted`     TINYINT NOT NULL DEFAULT 0,
  `create_by`   BIGINT DEFAULT NULL,
  `update_by`   BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 字典表（新增）
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `code`        VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort`        INT NOT NULL DEFAULT 0,
  `status`      TINYINT NOT NULL DEFAULT 1,
  `description` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deleted`     TINYINT NOT NULL DEFAULT 0,
  `create_by`   BIGINT DEFAULT NULL,
  `update_by`   BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 字典数据表（新增）
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,
  `dict_id`     BIGINT NOT NULL,
  `dict_code`   VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name`        VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value`       VARCHAR(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `color`       VARCHAR(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sort`        INT NOT NULL DEFAULT 0,
  `status`      TINYINT NOT NULL DEFAULT 1,
  `deleted`     TINYINT NOT NULL DEFAULT 0,
  `create_by`   BIGINT DEFAULT NULL,
  `update_by`   BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_dict_id` (`dict_id`),
  KEY `idx_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户表（扩展字段）
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,
  `username`    VARCHAR(64)  COLLATE utf8mb4_unicode_ci NOT NULL,
  `password`    VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname`    VARCHAR(64)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar`      VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email`       VARCHAR(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone`       VARCHAR(20)  COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gender`      TINYINT DEFAULT 1 COMMENT '1=男 2=女',
  `dept_id`     BIGINT DEFAULT NULL,
  `user_type`   TINYINT DEFAULT 1 COMMENT '1=普通用户 2=超级管理员',
  `description` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status`      TINYINT NOT NULL DEFAULT 1,
  `deleted`     TINYINT NOT NULL DEFAULT 0,
  `create_by`   BIGINT DEFAULT NULL,
  `update_by`   BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status_deleted` (`status`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户角色关联表（已有）
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始数据
INSERT INTO `sys_dept` (`id`, `parent_id`, `name`, `sort`, `status`) VALUES
(1, 0, 'XXX科技有限公司', 0, 1),
(2, 1, '广州总部', 0, 1),
(3, 2, '研发部', 0, 1),
(4, 3, '研发一组', 0, 1),
(5, 3, '研发二组', 1, 1),
(6, 3, '研发三组', 2, 0),
(7, 2, 'UI部', 1, 1),
(8, 2, '测试部', 2, 1),
(9, 2, '运维部', 3, 1);

INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `sort`, `status`, `remark`) VALUES
(1, '超级管理员', 'role_admin', 0, 1, '系统超级管理员'),
(2, '普通用户', 'role_user', 1, 1, '普通用户角色');

INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `gender`, `dept_id`, `user_type`, `status`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSn9qdB.SkKouBEAzWkBMVABhOGvRNGBOz.dYdXHPpua', '管理员', 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png', 1, 1, 2, 1),
(2, 'user', '$2a$10$7JB720yubVSn9qdB.SkKouBEAzWkBMVABhOGvRNGBOz.dYdXHPpua', '木糖醇', 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png', 1, 4, 1, 1);

INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1), (2, 2);
