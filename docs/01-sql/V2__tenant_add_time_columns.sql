-- V2: sys_tenant 表新增 start_time 和 end_time 列
ALTER TABLE sys_tenant ADD COLUMN start_time DATETIME DEFAULT NULL COMMENT '开始时间';
ALTER TABLE sys_tenant ADD COLUMN end_time DATETIME DEFAULT NULL COMMENT '结束时间';
