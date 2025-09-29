package com.kava.kbpd.upms.domain.repository;

import com.kava.kbpd.common.core.base.IBaseSimpleRepository;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientDetailsId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientDetailsListQuery;

public interface ISysOauthClientDetailsRepository extends IBaseSimpleRepository<SysOauthClientDetailsId, SysOauthClientDetailsEntity, SysOauthClientDetailsListQuery> {

}