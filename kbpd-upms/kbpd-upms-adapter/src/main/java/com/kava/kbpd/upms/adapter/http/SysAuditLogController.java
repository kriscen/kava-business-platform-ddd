package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysAuditLogAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysAuditLogAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysAuditLogRequest;
import com.kava.kbpd.upms.api.model.response.SysAuditLogDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysAuditLogListResponse;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAuditLogAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAuditLogAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysAuditLogId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/audit-log/")
public class SysAuditLogController {
    @Resource
    private ISysAuditLogAppService appService;
    @Resource
    private SysAuditLogAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAuditLogListResponse>> getSysAuditLogPage(@ModelAttribute SysAuditLogAdapterListQuery query) {
        PagingInfo<SysAuditLogAppListDTO> sysAuditLogEntityPagingInfo = appService.queryAuditLogPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysAuditLogListResponse> result = PagingInfo.toResponse(sysAuditLogEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysAuditLogEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysAuditLogDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysAuditLogAppDetailDTO sysAuditLogEntity = appService.queryAuditLogById(SysAuditLogId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysAuditLogEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysAuditLogRequest req) {
        SysAuditLogId sysAuditLogId = appService.createAuditLog(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysAuditLogId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysAuditLogRequest req) {
        req.setId(id);
        appService.updateAuditLog(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysAuditLogId> idList = ids.stream().map(SysAuditLogId::of).toList();
        appService.removeAuditLogBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}