package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysLogQuery;
import com.kava.kbpd.upms.api.model.request.SysLogRequest;
import com.kava.kbpd.upms.api.model.response.SysLogListResponse;
import com.kava.kbpd.upms.api.model.response.SysLogResponse;
import com.kava.kbpd.upms.domain.model.entity.SysLogEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysLogId;
import com.kava.kbpd.upms.domain.model.valobj.SysLogListQuery;
import com.kava.kbpd.upms.domain.service.ISysLogService;
import com.kava.kbpd.upms.adapter.converter.SysLogAdapterConverter;
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
    private ISysLogService sysLogService;
    @Resource
    private SysLogAdapterConverter sysLogTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysLogListResponse>> getSysLogPage(SysLogQuery query) {
        SysLogListQuery q = sysLogTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysLogEntity> pagingInfo = sysLogService.queryPage(q);
        PagingInfo<SysLogListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysLogTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysLogResponse> getDetails(Long id) {
        SysLogEntity sysLog = sysLogService.queryById(SysLogId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysLogTriggerConverter.convertEntity2Detail(sysLog));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysLogRequest req) {
        SysLogId id = sysLogService.create(sysLogTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysLogRequest req) {
        return JsonResult.buildSuccess(sysLogService.update(sysLogTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysLogId> idList = ids.stream().map(t->SysLogId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysLogService.removeBatchByIds(idList));
    }
}