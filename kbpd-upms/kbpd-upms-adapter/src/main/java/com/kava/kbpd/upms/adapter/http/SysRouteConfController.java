package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysRouteConfAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysRouteConfAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysRouteConfRequest;
import com.kava.kbpd.upms.api.model.response.SysRouteConfDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysRouteConfListResponse;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRouteConfAppListDTO;
import com.kava.kbpd.upms.application.service.ISysRouteConfAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysRouteConfId;
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
    private ISysRouteConfAppService appService;
    @Resource
    private SysRouteConfAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysRouteConfListResponse>> getSysRouteConfPage(@ModelAttribute SysRouteConfAdapterListQuery query) {
        PagingInfo<SysRouteConfAppListDTO> sysRouteConfEntityPagingInfo = appService.queryRouteConfPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysRouteConfListResponse> result = PagingInfo.toResponse(sysRouteConfEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysRouteConfEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysRouteConfDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysRouteConfAppDetailDTO sysRouteConfEntity = appService.queryRouteConfById(SysRouteConfId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysRouteConfEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysRouteConfRequest req) {
        SysRouteConfId sysRouteConfId = appService.createRouteConf(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysRouteConfId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysRouteConfRequest req) {
        req.setId(id);
        appService.updateRouteConf(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysRouteConfId> idList = ids.stream().map(SysRouteConfId::of).toList();
        appService.removeRouteConfBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}