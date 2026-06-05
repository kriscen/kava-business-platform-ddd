package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysAuditLogAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysAuditLogAdapterListQuery;
import com.kava.kbpd.upms.api.model.response.SysAuditLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAuditLogListResponse;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAuditLogAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/audit-log")
public class SysAuditLogController {
    @Resource
    private ISysAuditLogAppService appService;
    @Resource
    private SysAuditLogAdapterConverter adapterConverter;

    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAuditLogListResponse>> getSysAuditLogPage(@ModelAttribute SysAuditLogAdapterListQuery query) {
        PagingInfo<SysAuditLogAppListDTO> sysAuditLogEntityPagingInfo = appService.queryAuditLogPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysAuditLogListResponse> result = PagingInfo.toResponse(sysAuditLogEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysAuditLogEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    @GetMapping("/{id}")
    public JsonResult<SysAuditLogDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysAuditLogAppDetailDTO sysAuditLogEntity = appService.queryAuditLogById(SysAuditLogId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysAuditLogEntity));
    }
}
