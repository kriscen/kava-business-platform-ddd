package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysTenantQuery;
import com.kava.kbpd.upms.api.model.request.SysTenantRequest;
import com.kava.kbpd.upms.api.model.response.SysTenantListResponse;
import com.kava.kbpd.upms.api.model.response.SysTenantResponse;
import com.kava.kbpd.upms.domain.model.entity.SysTenantEntity;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.domain.model.valobj.SysTenantListQuery;
import com.kava.kbpd.upms.domain.service.ISysTenantService;
import com.kava.kbpd.upms.adapter.converter.SysTenantAdapterConverter;
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
    private ISysTenantService sysTenantService;
    @Resource
    private SysTenantAdapterConverter sysTenantTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysTenantListResponse>> getSysAreaPage(SysTenantQuery query) {
        SysTenantListQuery q = sysTenantTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysTenantEntity> pagingInfo = sysTenantService.queryPage(q);
        PagingInfo<SysTenantListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysTenantTriggerConverter::convertEntity2List).toList(),
                        pagingInfo);
        return JsonResult.buildSuccess(result);
    }


    /**
     * 获取详细信息
     *
     * @param id 查询id
     * @return 明细
     */
    @GetMapping("/details")
    public JsonResult<SysTenantResponse> getDetails(Long id) {
        SysTenantEntity sysTenant = sysTenantService.queryById(SysTenantId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysTenantTriggerConverter.convertEntity2Detail(sysTenant));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysTenantRequest req) {
        SysTenantId id = sysTenantService.create(sysTenantTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysTenantRequest req) {
        return JsonResult.buildSuccess(sysTenantService.update(sysTenantTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysTenantId> idList = ids.stream().map(t->SysTenantId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysTenantService.removeBatchByIds(idList));
    }

}