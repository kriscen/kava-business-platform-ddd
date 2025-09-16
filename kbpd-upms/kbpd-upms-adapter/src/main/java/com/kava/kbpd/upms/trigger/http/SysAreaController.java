package com.kava.kbpd.upms.trigger.http;

import cn.hutool.core.lang.tree.Tree;
import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysAreaQuery;
import com.kava.kbpd.upms.api.model.request.SysAreaRequest;
import com.kava.kbpd.upms.api.model.response.SysAreaListResponse;
import com.kava.kbpd.upms.api.model.response.SysAreaResponse;
import com.kava.kbpd.upms.domain.basic.model.entity.SysAreaEntity;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.basic.model.valobj.SysAreaListQuery;
import com.kava.kbpd.upms.domain.basic.service.ISysAreaService;
import com.kava.kbpd.upms.trigger.converter.SysAreaTriggerConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kris
 * @date 2025/3/26
 * @description: 行政区划 controller
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/area/")
public class SysAreaController {
    @Resource
    private ISysAreaService sysAreaService;
    @Resource
    private SysAreaTriggerConverter sysAreaTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAreaListResponse>> getSysAreaPage(SysAreaQuery query) {
        SysAreaListQuery q = sysAreaTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysAreaEntity> sysAreaEntityPagingInfo = sysAreaService.queryPage(q);
        PagingInfo<SysAreaListResponse> result = PagingInfo.toResponse(sysAreaEntityPagingInfo.getList().stream().
                                                    map(sysAreaTriggerConverter::convertEntity2List).toList(),
                                                    sysAreaEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 前端联动组件需要数据
     *
     * @param query 查询条件
     * @return tree
     */
    @GetMapping("/tree")
    public JsonResult<List<Tree<Long>>> getSysAreaTree(SysAreaQuery query) {
        SysAreaListQuery q = sysAreaTriggerConverter.convertQueryDTO2QueryVal(query);
        List<Tree<Long>> result = sysAreaService.selectTree(q);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/details")
    public JsonResult<SysAreaResponse> getDetails(Long id) {
        SysAreaEntity sysAreaEntity = sysAreaService.queryById(SysAreaId.builder()
                                                    .id(id)
                                                    .build());
        return JsonResult.buildSuccess(sysAreaTriggerConverter.convertEntity2Detail(sysAreaEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysAreaRequest req) {
        SysAreaId sysAreaId = sysAreaService.create(sysAreaTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(sysAreaId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysAreaRequest req) {
        return JsonResult.buildSuccess(sysAreaService.update(sysAreaTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysAreaId> idList = ids.stream().map(t->SysAreaId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysAreaService.removeBatchByIds(idList));
    }

}
