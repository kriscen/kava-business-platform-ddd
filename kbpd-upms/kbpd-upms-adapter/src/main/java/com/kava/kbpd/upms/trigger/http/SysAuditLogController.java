package com.kava.kbpd.upms.trigger.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysAuditLogQuery;
import com.kava.kbpd.upms.api.model.request.SysAuditLogRequest;
import com.kava.kbpd.upms.api.model.response.SysAuditLogListResponse;
import com.kava.kbpd.upms.api.model.response.SysAuditLogResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAuditLogEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAuditLogListQuery;
import com.kava.kbpd.upms.domain.basic.service.ISysAuditLogService;
import com.kava.kbpd.upms.trigger.converter.SysAuditLogTriggerConverter;
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
    private ISysAuditLogService sysAuditLogService;
    @Resource
    private SysAuditLogTriggerConverter sysAuditLogTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAuditLogListResponse>> getSysAreaPage(SysAuditLogQuery query) {
        SysAuditLogListQuery q = sysAuditLogTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysAuditLogEntity> pagingInfo = sysAuditLogService.queryPage(q);
        PagingInfo<SysAuditLogListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysAuditLogTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysAuditLogResponse> getDetails(Long id) {
        SysAuditLogEntity sysAuditLog = sysAuditLogService.queryById(SysAuditLogId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysAuditLogTriggerConverter.convertEntity2Detail(sysAuditLog));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysAuditLogRequest req) {
        SysAuditLogId id = sysAuditLogService.create(sysAuditLogTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysAuditLogRequest req) {
        return JsonResult.buildSuccess(sysAuditLogService.update(sysAuditLogTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysAuditLogId> idList = ids.stream().map(t->SysAuditLogId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysAuditLogService.removeBatchByIds(idList));
    }


}