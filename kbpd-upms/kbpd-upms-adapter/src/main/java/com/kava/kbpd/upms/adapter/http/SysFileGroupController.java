package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysFileGroupAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysFileGroupQuery;
import com.kava.kbpd.upms.api.model.request.SysFileGroupRequest;
import com.kava.kbpd.upms.api.model.response.SysFileGroupDetailResponse;
import com.kava.kbpd.upms.domain.model.entity.SysFileGroupEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupListQuery;
import com.kava.kbpd.upms.domain.service.ISysFileGroupService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/file-group/")
public class SysFileGroupController {
    @Resource
    private ISysFileGroupService sysFileGroupService;
    @Resource
    private SysFileGroupAdapterConverter sysFileGroupTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysFileGroupDetailResponse>> getSysFileGroupPage(SysFileGroupQuery query) {
        SysFileGroupListQuery q = sysFileGroupTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysFileGroupEntity> pagingInfo = sysFileGroupService.queryPage(q);
        PagingInfo<SysFileGroupDetailResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysFileGroupTriggerConverter::SysFileGroupResponse).toList(),
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
    public JsonResult<SysFileGroupDetailResponse> getDetails(Long id) {
        SysFileGroupEntity sysFileGroup = sysFileGroupService.queryById(SysFileGroupId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysFileGroupTriggerConverter.SysFileGroupResponse(sysFileGroup));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysFileGroupRequest req) {
        SysFileGroupId id = sysFileGroupService.create(sysFileGroupTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysFileGroupRequest req) {
        return JsonResult.buildSuccess(sysFileGroupService.update(sysFileGroupTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysFileGroupId> idList = ids.stream().map(t -> SysFileGroupId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysFileGroupService.removeBatchByIds(idList));
    }
}