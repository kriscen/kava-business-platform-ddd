package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysLogAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysLogAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysLogRequest;
import com.kava.kbpd.upms.api.model.response.SysLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysLogListResponse;
import com.kava.kbpd.upms.application.model.dto.SysLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysLogAppListDTO;
import com.kava.kbpd.upms.application.service.ISysLogAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/log/")
public class SysLogController {
    @Resource
    private ISysLogAppService appService;
    @Resource
    private SysLogAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysLogListResponse>> getSysLogPage(@ModelAttribute SysLogAdapterListQuery query) {
        PagingInfo<SysLogAppListDTO> sysLogEntityPagingInfo = appService.queryLogPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysLogListResponse> result = PagingInfo.toResponse(sysLogEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysLogEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysLogDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysLogAppDetailDTO sysLogEntity = appService.queryLogById(SysLogId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysLogEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysLogRequest req) {
        SysLogId sysLogId = appService.createLog(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysLogId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysLogRequest req) {
        req.setId(id);
        appService.updateLog(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysLogId> idList = ids.stream().map(SysLogId::of).toList();
        appService.removeLogBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}