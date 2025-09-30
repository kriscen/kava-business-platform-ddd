package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysPublicParamAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysPublicParamAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysPublicParamRequest;
import com.kava.kbpd.upms.api.model.response.SysPublicParamDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysPublicParamListResponse;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysPublicParamAppListDTO;
import com.kava.kbpd.upms.application.service.ISysPublicParamAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/public-param/")
public class SysPublicParamController {
    @Resource
    private ISysPublicParamAppService appService;
    @Resource
    private SysPublicParamAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysPublicParamListResponse>> getSysPublicParamPage(@ModelAttribute SysPublicParamAdapterListQuery query) {
        PagingInfo<SysPublicParamAppListDTO> sysPublicParamEntityPagingInfo = appService.queryPublicParamPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysPublicParamListResponse> result = PagingInfo.toResponse(sysPublicParamEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysPublicParamEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysPublicParamDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysPublicParamAppDetailDTO sysPublicParamEntity = appService.queryPublicParamById(SysPublicParamId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysPublicParamEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysPublicParamRequest req) {
        SysPublicParamId sysPublicParamId = appService.createPublicParam(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysPublicParamId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysPublicParamRequest req) {
        req.setId(id);
        appService.updatePublicParam(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysPublicParamId> idList = ids.stream().map(SysPublicParamId::of).toList();
        appService.removePublicParamBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}