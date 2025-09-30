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
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/i18n/")
public class SysI18nController {
    @Resource
    private ISysI18nAppService appService;
    @Resource
    private SysI18nAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysI18nListResponse>> getSysI18nPage(@ModelAttribute SysI18nAdapterListQuery query) {
        PagingInfo<SysI18nAppListDTO> sysI18nEntityPagingInfo = appService.queryI18nPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysI18nListResponse> result = PagingInfo.toResponse(sysI18nEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysI18nEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysI18nDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysI18nAppDetailDTO sysI18nEntity = appService.queryI18nById(SysI18nId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysI18nEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysI18nRequest req) {
        SysI18nId sysI18nId = appService.createI18n(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysI18nId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysI18nRequest req) {
        req.setId(id);
        appService.updateI18n(adapterConverter.convertRequest2UpdateCommand(req));
        return JsonResult.buildSuccess();
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Void> removeById(@RequestBody List<Long> ids) {
        List<SysI18nId> idList = ids.stream().map(SysI18nId::of).toList();
        appService.removeI18nBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}