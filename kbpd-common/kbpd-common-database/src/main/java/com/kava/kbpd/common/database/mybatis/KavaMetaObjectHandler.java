package com.kava.kbpd.common.database.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.kava.kbpd.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class KavaMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String user = getCurrentUser();
        this.strictInsertFill(metaObject, "creator", String.class, user);
        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "modifier", String.class, user);
        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "delFlag", String.class, "0");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "modifier", String.class, getCurrentUser());
        this.strictUpdateFill(metaObject, "gmtModified", LocalDateTime.class, LocalDateTime.now());
    }

    private String getCurrentUser() {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                return String.valueOf(userId);
            }
        } catch (Exception ignored) {
        }
        return "system";
    }
}
