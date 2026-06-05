package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysLogAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysLogAdapterListQuery;
import com.kava.kbpd.upms.api.model.response.SysLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysLogListResponse;
import com.kava.kbpd.upms.application.model.dto.SysLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysLogAppListDTO;
import com.kava.kbpd.upms.application.service.ISysLogAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/log")
public class SysLogController {
    @Resource
    private ISysLogAppService appService;
    @Resource
    private SysLogAdapterConverter adapterConverter;

    @GetMapping("/page")
    public JsonResult<PagingInfo<SysLogListResponse>> getSysLogPage(@ModelAttribute SysLogAdapterListQuery query) {
        PagingInfo<SysLogAppListDTO> sysLogEntityPagingInfo = appService.queryLogPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysLogListResponse> result = PagingInfo.toResponse(sysLogEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysLogEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    @GetMapping("/{id}")
    public JsonResult<SysLogDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysLogAppDetailDTO sysLogEntity = appService.queryLogById(SysLogId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysLogEntity));
    }
}
