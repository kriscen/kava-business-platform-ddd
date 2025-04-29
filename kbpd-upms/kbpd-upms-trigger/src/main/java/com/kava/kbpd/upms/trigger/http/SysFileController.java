package com.kava.kbpd.upms.trigger.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysFileQuery;
import com.kava.kbpd.upms.api.model.request.SysFileRequest;
import com.kava.kbpd.upms.api.model.response.SysFileListResponse;
import com.kava.kbpd.upms.api.model.response.SysFileResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysFileEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysFileListQuery;
import com.kava.kbpd.upms.domain.basic.service.ISysFileService;
import com.kava.kbpd.upms.trigger.converter.SysFileTriggerConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/file/")
public class SysFileController {
    @Resource
    private ISysFileService sysFileService;
    @Resource
    private SysFileTriggerConverter sysFileTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysFileListResponse>> getSysFilePage(SysFileQuery query) {
        SysFileListQuery q = sysFileTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysFileEntity> pagingInfo = sysFileService.queryPage(q);
        PagingInfo<SysFileListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysFileTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysFileResponse> getDetails(Long id) {
        SysFileEntity sysFile = sysFileService.queryById(SysFileId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysFileTriggerConverter.convertEntity2Detail(sysFile));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysFileRequest req) {
        SysFileId id = sysFileService.create(sysFileTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysFileRequest req) {
        return JsonResult.buildSuccess(sysFileService.update(sysFileTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysFileId> idList = ids.stream().map(t -> SysFileId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysFileService.removeBatchByIds(idList));
    }
}