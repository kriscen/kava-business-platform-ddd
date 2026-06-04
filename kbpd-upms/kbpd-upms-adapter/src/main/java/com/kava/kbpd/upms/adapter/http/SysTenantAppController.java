package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.upms.adapter.converter.SysTenantAppAdapterConverter;
import com.kava.kbpd.upms.api.model.request.SysTenantAppRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantAppListResponse;
import com.kava.kbpd.upms.application.model.dto.TenantAppListDTO;
import com.kava.kbpd.upms.application.service.ISysTenantAppAppService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/tenant-app/")
public class SysTenantAppController {
    @Resource
    private ISysTenantAppAppService appService;
    @Resource
    private SysTenantAppAdapterConverter adapterConverter;

    @PostMapping("/subscribe")
    public JsonResult<Void> subscribe(@RequestBody SysTenantAppRequest req) {
        appService.subscribe(req.getTenantId(), req.getAppId());
        return JsonResult.buildSuccess();
    }

    @PostMapping("/unsubscribe")
    public JsonResult<Void> unsubscribe(@RequestBody SysTenantAppRequest req) {
        appService.unsubscribe(req.getTenantId(), req.getAppId());
        return JsonResult.buildSuccess();
    }

    @GetMapping("/tenant/{tenantId}")
    public JsonResult<List<SysTenantAppListResponse>> getByTenantId(@PathVariable("tenantId") Long tenantId) {
        List<TenantAppListDTO> dtos = appService.queryByTenantId(tenantId);
        List<SysTenantAppListResponse> result = dtos.stream()
                .map(adapterConverter::convertEntity2List)
                .toList();
        return JsonResult.buildSuccess(result);
    }
}
