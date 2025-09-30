package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysRouteConfQuery;
import com.kava.kbpd.upms.api.model.request.SysRouteConfRequest;
import com.kava.kbpd.upms.api.model.response.SysRouteConfDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysRouteConfListResponse;
import com.kava.kbpd.upms.domain.model.entity.SysRouteConfEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfListQuery;
import com.kava.kbpd.upms.domain.service.ISysRouteConfService;
import com.kava.kbpd.upms.adapter.converter.SysRouteConfAdapterConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/route-conf/")
public class SysRouteConfController {
    @Resource
    private ISysRouteConfService sysRouteConfService;
    @Resource
    private SysRouteConfAdapterConverter sysRouteConfTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysRouteConfListResponse>> getSysAreaPage(SysRouteConfQuery query) {
        SysRouteConfListQuery q = sysRouteConfTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysRouteConfEntity> pagingInfo = sysRouteConfService.queryPage(q);
        PagingInfo<SysRouteConfListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysRouteConfTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysRouteConfDetailResponse> getDetails(Long id) {
        SysRouteConfEntity sysRouteConf = sysRouteConfService.queryById(SysRouteConfId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysRouteConfTriggerConverter.convertEntity2Detail(sysRouteConf));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysRouteConfRequest req) {
        SysRouteConfId id = sysRouteConfService.create(sysRouteConfTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysRouteConfRequest req) {
        return JsonResult.buildSuccess(sysRouteConfService.update(sysRouteConfTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysRouteConfId> idList = ids.stream().map(t->SysRouteConfId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysRouteConfService.removeBatchByIds(idList));
    }

}