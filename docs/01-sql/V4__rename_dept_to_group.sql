-- V4: Rename dept → group (semantic rename for family SaaS platform)

-- 1. Rename table sys_dept → sys_group
RENAME TABLE sys_dept TO sys_group;

-- 2. Rename column in sys_user: dept_id → group_id
ALTER TABLE sys_user RENAME COLUMN dept_id TO group_id;
