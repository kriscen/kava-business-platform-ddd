package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.adapter.converter.SysTenantAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysTenantAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysTenantRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantListResponse;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysTenantAppListDTO;
import com.kava.kbpd.upms.application.service.ISysTenantAppService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/tenant/")
public class SysTenantController {
    @Resource
    private ISysTenantAppService appService;
    @Resource
    private SysTenantAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysTenantListResponse>> getSysTenantPage(@ModelAttribute SysTenantAdapterListQuery query) {
        PagingInfo<SysTenantAppListDTO> sysTenantEntityPagingInfo = appService.queryTenantPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysTenantListResponse> result = PagingInfo.toResponse(sysTenantEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysTenantEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysTenantDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysTenantAppDetailDTO sysTenantEntity = appService.queryTenantById(SysTenantId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysTenantEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysTenantRequest req) {
        SysTenantId sysTenantId = appService.createTenant(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysTenantId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysTenantRequest req) {
        req.setId(id);
        appService.updateTenant(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysTenantId> idList = ids.stream().map(SysTenantId::of).toList();
        appService.removeTenantBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}