package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysMenuQuery;
import com.kava.kbpd.upms.api.model.request.SysMenuRequest;
import com.kava.kbpd.upms.api.model.response.SysMenuDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysMenuListResponse;
import com.kava.kbpd.upms.domain.model.entity.SysMenuEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuListQuery;
import com.kava.kbpd.upms.domain.service.ISysMenuService;
import com.kava.kbpd.upms.adapter.converter.SysMenuAdapterConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/menu/")
public class SysMenuController {
    @Resource
    private ISysMenuService sysMenuService;
    @Resource
    private SysMenuAdapterConverter sysMenuTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysMenuListResponse>> getSysMenuPage(SysMenuQuery query) {
        SysMenuListQuery q = sysMenuTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysMenuEntity> pagingInfo = sysMenuService.queryPage(q);
        PagingInfo<SysMenuListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysMenuTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysMenuDetailResponse> getDetails(Long id) {
        SysMenuEntity sysMenu = sysMenuService.queryById(SysMenuId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysMenuTriggerConverter.convertEntity2Detail(sysMenu));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysMenuRequest req) {
        SysMenuId id = sysMenuService.create(sysMenuTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysMenuRequest req) {
        return JsonResult.buildSuccess(sysMenuService.update(sysMenuTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysMenuId> idList = ids.stream().map(t -> SysMenuId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysMenuService.removeBatchByIds(idList));
    }
}