package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysAppAdapterConverter;
import com.kava.kbpd.upms.api.model.request.SysAppRequest;
import com.kava.kbpd.upms.api.model.response.SysAppDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAppDropdownResponse;
import com.kava.kbpd.upms.api.model.response.SysAppListResponse;
import com.kava.kbpd.upms.application.model.dto.SysAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAppAppService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/app/")
public class SysAppController {
    @Resource
    private ISysAppAppService appService;
    @Resource
    private SysAppAdapterConverter adapterConverter;

    @PostMapping
    public JsonResult<Long> create(@RequestBody SysAppRequest req) {
        return JsonResult.buildSuccess(appService.createApp(adapterConverter.convertRequest2CreateCommand(req)));
    }

    @PutMapping("/{id}")
    public JsonResult<Void> update(@PathVariable("id") Long id, @RequestBody SysAppRequest req) {
        req.setId(id);
        appService.updateApp(adapterConverter.convertRequest2UpdateCommand(req));
        return JsonResult.buildSuccess();
    }

    @DeleteMapping
    public JsonResult<Void> remove(@RequestBody List<Long> ids) {
        appService.removeAppBatchByIds(ids);
        return JsonResult.buildSuccess();
    }

    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAppListResponse>> getPage(
            @RequestParam(required = false) String appName,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PagingInfo<SysAppListDTO> pagingInfo = appService.queryAppPage(appName, pageNo, pageSize);
        PagingInfo<SysAppListResponse> result = PagingInfo.toResponse(
                pagingInfo.getList().stream().map(adapterConverter::convertEntity2List).toList(),
                pagingInfo);
        return JsonResult.buildSuccess(result);
    }

    @GetMapping("/{id}")
    public JsonResult<SysAppDetailResponse> getById(@PathVariable("id") Long id) {
        SysAppDetailDTO dto = appService.queryAppById(id);
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(dto));
    }

    @GetMapping("/dropdown")
    public JsonResult<List<SysAppDropdownResponse>> getDropdown() {
        List<SysAppListDTO> apps = appService.queryAppDropdown();
        List<SysAppDropdownResponse> result = apps.stream()
                .map(adapterConverter::convertEntity2Dropdown)
                .toList();
        return JsonResult.buildSuccess(result);
    }

    @PutMapping("/{id}/menus")
    public JsonResult<Void> updateAppMenus(@PathVariable("id") Long id, @RequestBody List<Long> menuIds) {
        appService.updateAppMenus(id, menuIds);
        return JsonResult.buildSuccess();
    }
}
