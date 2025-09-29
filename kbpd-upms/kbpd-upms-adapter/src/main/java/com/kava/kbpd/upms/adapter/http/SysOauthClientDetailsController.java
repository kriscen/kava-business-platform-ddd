package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysOauthClientDetailsQuery;
import com.kava.kbpd.upms.api.model.request.SysOauthClientDetailsRequest;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailsListResponse;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailsResponse;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientDetailsEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientDetailsId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientDetailsListQuery;
import com.kava.kbpd.upms.domain.service.ISysOauthClientDetailsService;
import com.kava.kbpd.upms.adapter.converter.SysOauthClientDetailsAdapterConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/oauth-client-details/")
public class SysOauthClientDetailsController {
    @Resource
    private ISysOauthClientDetailsService sysOauthClientDetailsService;
    @Resource
    private SysOauthClientDetailsAdapterConverter sysOauthClientDetailsTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysOauthClientDetailsListResponse>> getSysOauthClientDetailsPage(SysOauthClientDetailsQuery query) {
        SysOauthClientDetailsListQuery q = sysOauthClientDetailsTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysOauthClientDetailsEntity> pagingInfo = sysOauthClientDetailsService.queryPage(q);
        PagingInfo<SysOauthClientDetailsListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysOauthClientDetailsTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysOauthClientDetailsResponse> getDetails(Long id) {
        SysOauthClientDetailsEntity sysOauthClientDetails = sysOauthClientDetailsService.queryById(SysOauthClientDetailsId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysOauthClientDetailsTriggerConverter.convertEntity2Detail(sysOauthClientDetails));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysOauthClientDetailsRequest req) {
        SysOauthClientDetailsId id = sysOauthClientDetailsService.create(sysOauthClientDetailsTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysOauthClientDetailsRequest req) {
        return JsonResult.buildSuccess(sysOauthClientDetailsService.update(sysOauthClientDetailsTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysOauthClientDetailsId> idList = ids.stream().map(t->SysOauthClientDetailsId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysOauthClientDetailsService.removeBatchByIds(idList));
    }
}