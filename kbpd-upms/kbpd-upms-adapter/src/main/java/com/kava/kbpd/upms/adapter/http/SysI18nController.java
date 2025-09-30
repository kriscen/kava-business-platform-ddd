package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysI18nQuery;
import com.kava.kbpd.upms.api.model.request.SysI18nRequest;
import com.kava.kbpd.upms.api.model.response.SysI18nDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysI18nEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nId;
import com.kava.kbpd.upms.domain.model.valobj.SysI18nListQuery;
import com.kava.kbpd.upms.domain.service.ISysI18nService;
import com.kava.kbpd.upms.adapter.converter.SysI18nAdapterConverter;
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
    private ISysI18nService sysI18nService;
    @Resource
    private SysI18nAdapterConverter sysI18nTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysI18nDetailResponse>> getSysI18nPage(SysI18nQuery query) {
        SysI18nListQuery q = sysI18nTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysI18nEntity> pagingInfo = sysI18nService.queryPage(q);
        PagingInfo<SysI18nDetailResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysI18nTriggerConverter::convertEntity2Resp).toList(),
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
    public JsonResult<SysI18nDetailResponse> getDetails(Long id) {
        SysI18nEntity sysI18n = sysI18nService.queryById(SysI18nId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysI18nTriggerConverter.convertEntity2Resp(sysI18n));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysI18nRequest req) {
        SysI18nId id = sysI18nService.create(sysI18nTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysI18nRequest req) {
        return JsonResult.buildSuccess(sysI18nService.update(sysI18nTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysI18nId> idList = ids.stream().map(t->SysI18nId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysI18nService.removeBatchByIds(idList));
    }
}