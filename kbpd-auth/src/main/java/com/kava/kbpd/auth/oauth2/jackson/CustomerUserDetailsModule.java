package com.kava.kbpd.auth.oauth2.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.kava.kbpd.auth.model.MemberDetails;
import com.kava.kbpd.auth.model.SysUserDetails;

/**
 * @author Kris
 * @date 2025/4/19
 * @description:
 */
public class CustomerUserDetailsModule extends SimpleModule {
    public CustomerUserDetailsModule() {
        super(CustomerUserDetailsModule.class.getName());
        this.setMixInAnnotation(MemberDetails.class,MemberMixin.class);
        this.setMixInAnnotation(SysUserDetails.class,SysUserMixin.class);
    }
}
