package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysOauthClientAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysOauthClientDetailsQuery;
import com.kava.kbpd.upms.api.model.request.SysOauthClientDetailsRequest;
import com.kava.kbpd.upms.api.model.response.SysOauthClientListResponse;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysOauthClientEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientListQuery;
import com.kava.kbpd.upms.domain.service.ISysOauthClientService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/oauth-client-details/")
public class SysOauthClientController {
    @Resource
    private ISysOauthClientService sysOauthClientDetailsService;
    @Resource
    private SysOauthClientAdapterConverter sysOauthClientDetailsTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysOauthClientListResponse>> getSysOauthClientDetailsPage(SysOauthClientDetailsQuery query) {
        SysOauthClientListQuery q = sysOauthClientDetailsTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysOauthClientEntity> pagingInfo = sysOauthClientDetailsService.queryPage(q);
        PagingInfo<SysOauthClientListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
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
    public JsonResult<SysOauthClientDetailResponse> getDetails(Long id) {
        SysOauthClientEntity sysOauthClientDetails = sysOauthClientDetailsService.queryById(SysOauthClientId.builder()
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
        SysOauthClientId id = sysOauthClientDetailsService.create(sysOauthClientDetailsTriggerConverter.convertRequest2Entity(req));
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
        List<SysOauthClientId> idList = ids.stream().map(t-> SysOauthClientId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysOauthClientDetailsService.removeBatchByIds(idList));
    }
}