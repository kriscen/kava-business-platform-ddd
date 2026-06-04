-- ============================================================
-- Migration: menu-app-model
-- Description: 引入 App 概念，scope→level，声明式菜单分发
-- ============================================================

-- 1. 新增 sys_app 表
CREATE TABLE IF NOT EXISTS `sys_app` (
    `id` bigint NOT NULL COMMENT '应用ID',
    `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用编码',
    `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
    `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用图标',
    `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用描述',
    `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '应用状态，ACTIVE可用，DISABLED停用',
    `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modifier` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者',
    `gmt_modified` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用管理表';

-- 2. 新增 sys_app_menu 表
CREATE TABLE IF NOT EXISTS `sys_app_menu` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `app_id` bigint NOT NULL COMMENT '应用ID',
    `menu_id` bigint NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_app_id` (`app_id`) USING BTREE,
    KEY `idx_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应用菜单关联表';

-- 3. 新增 sys_tenant_app 表
CREATE TABLE IF NOT EXISTS `sys_tenant_app` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `tenant_id` bigint NOT NULL COMMENT '租户ID',
    `app_id` bigint NOT NULL COMMENT '应用ID',
    `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'ACTIVE' COMMENT '订阅状态，ACTIVE有效，EXPIRED过期',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modifier` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新者',
    `gmt_modified` datetime DEFAULT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_app` (`tenant_id`, `app_id`),
    KEY `idx_tenant_id` (`tenant_id`) USING BTREE,
    KEY `idx_app_id` (`app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户应用订阅表';

-- 4. sys_menu 表：scope 列重命名为 level
ALTER TABLE `sys_menu` CHANGE COLUMN `scope` `level` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单级别，PLATFORM平台级，TENANT租户级';

-- 5. sys_tenant 表：移除 menu_id 列
ALTER TABLE `sys_tenant` DROP COLUMN `menu_id`;

-- 6. 数据迁移：scope 值映射
-- SYSTEM → PLATFORM
UPDATE `sys_menu` SET `level` = 'PLATFORM' WHERE `level` = 'system';
-- TENANT → TENANT（值不变，保持大写）
UPDATE `sys_menu` SET `level` = 'TENANT' WHERE `level` = 'tenant';
-- SYSTEM_TENANT 拆分为两条记录
INSERT INTO `sys_menu` (`id`, `name`, `permission`, `path`, `component`, `parent_id`, `icon`, `visible`, `sort_order`, `keep_alive`, `embedded`, `menu_type`, `level`, `tenant_id`, `del_flag`, `creator`, `gmt_create`, `modifier`, `gmt_modified`)
SELECT
    `id` + 100000,
    `name`,
    `permission`,
    `path`,
    `component`,
    `parent_id`,
    `icon`,
    `visible`,
    `sort_order`,
    `keep_alive`,
    `embedded`,
    `menu_type`,
    'TENANT',
    `tenant_id`,
    `del_flag`,
    `creator`,
    `gmt_create`,
    `modifier`,
    `gmt_modified`
FROM `sys_menu`
WHERE `level` = 'system_tenant';

-- 将原 SYSTEM_TENANT 记录改为 PLATFORM
UPDATE `sys_menu` SET `level` = 'PLATFORM' WHERE `level` = 'system_tenant';

-- 更新 sys_role_menu 中引用拆分后 TENANT 版本菜单的角色绑定
-- 对于从 SYSTEM_TENANT 拆分出来的 TENANT 菜单（id + 100000），
-- 租户角色需要绑定这些 TENANT 版本（由应用层在角色分配时处理）

-- 7. 初始化 kava-base 系统应用
INSERT INTO `sys_app` (`id`, `code`, `name`, `icon`, `description`, `status`, `del_flag`, `creator`, `gmt_create`)
VALUES (1, 'kava-base', 'Kava 基础应用', NULL, '系统内置基础管理应用，包含平台管理和基础租户管理功能', 'ACTIVE', '0', 'system', NOW());

-- 关联 kava-base 与所有 PLATFORM 级菜单
INSERT INTO `sys_app_menu` (`id`, `app_id`, `menu_id`)
SELECT ROW_NUMBER() OVER () + (SELECT COALESCE(MAX(id), 0) FROM `sys_app_menu`),
       1, `id`
FROM `sys_menu`
WHERE `level` = 'PLATFORM' AND `del_flag` = '0';

-- 关联 kava-base 与所有 TENANT 级菜单（初始阶段，kava-base 包含基础 TENANT 菜单）
INSERT INTO `sys_app_menu` (`id`, `app_id`, `menu_id`)
SELECT ROW_NUMBER() OVER () + (SELECT COALESCE(MAX(id), 0) FROM `sys_app_menu`),
       1, `id`
FROM `sys_menu`
WHERE `level` = 'TENANT' AND `del_flag` = '0';

-- 8. 所有租户默认关联 kava-base
INSERT INTO `sys_tenant_app` (`id`, `tenant_id`, `app_id`, `status`, `creator`, `gmt_create`)
SELECT ROW_NUMBER() OVER () + (SELECT COALESCE(MAX(id), 0) FROM `sys_tenant_app`),
       `id`, 1, 'ACTIVE', 'system', NOW()
FROM `sys_tenant`
WHERE `del_flag` = '0';
