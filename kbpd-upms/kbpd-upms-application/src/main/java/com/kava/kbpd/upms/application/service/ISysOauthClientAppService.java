package com.kava.kbpd.upms.application.service;

import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.application.model.command.SysOauthClientCreateCommand;
import com.kava.kbpd.upms.application.model.command.SysOauthClientUpdateCommand;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppListDTO;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;

import java.util.List;

/**
 * @author Kris
 * @date 2025/9/28
 * @description: OauthClient application service
 */
public interface ISysOauthClientAppService {
    SysOauthClientId createOauthClient(SysOauthClientCreateCommand command);

    void updateOauthClient(SysOauthClientUpdateCommand command);

    void removeOauthClientBatchByIds(List<SysOauthClientId> ids);

    PagingInfo<SysOauthClientAppListDTO> queryOauthClientPage(SysOauthClientListQuery query);

    SysOauthClientAppDetailDTO queryOauthClientById(SysOauthClientId id);

    SysOauthClientAppDetailDTO queryByClientId(String clientId);
}
