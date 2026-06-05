package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysI18nAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysI18nAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysI18nRequest;
import com.kava.kbpd.upms.api.model.response.SysI18nDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysI18nListResponse;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysI18nAppListDTO;
import com.kava.kbpd.upms.application.service.ISysI18nAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nMessageId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/i18n")
@RequiredArgsConstructor
public class SysI18nController {
    private final ISysI18nAppService appService;
    private final SysI18nAdapterConverter adapterConverter;

    @GetMapping("/page")
    public JsonResult<PagingInfo<SysI18nListResponse>> getSysI18nPage(@ModelAttribute SysI18nAdapterListQuery query) {
        PagingInfo<SysI18nAppListDTO> page = appService.queryI18nPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysI18nListResponse> result = PagingInfo.toResponse(
                page.getList().stream().map(adapterConverter::convertEntity2List).toList(),
                page);
        return JsonResult.buildSuccess(result);
    }

    @GetMapping("/{id}")
    public JsonResult<SysI18nDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysI18nAppDetailDTO dto = appService.queryI18nById(SysI18nMessageId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(dto));
    }

    @PostMapping
    public JsonResult<Long> save(@RequestBody SysI18nRequest req) {
        SysI18nMessageId messageId = appService.createI18n(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(messageId.getId());
    }

    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id, @RequestBody SysI18nRequest req) {
        req.setId(id);
        appService.updateI18n(adapterConverter.convertRequest2UpdateCommand(req));
        return JsonResult.buildSuccess();
    }

    @DeleteMapping
    public JsonResult<Void> removeById(@RequestBody List<Long> ids) {
        List<SysI18nMessageId> idList = ids.stream().map(SysI18nMessageId::of).toList();
        appService.removeI18nBatchByIds(idList);
        return JsonResult.buildSuccess();
    }
}
