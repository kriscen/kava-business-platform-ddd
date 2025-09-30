package com.kava.kbpd.upms.adapter.http;

import cn.hutool.core.lang.tree.Tree;
import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysAreaAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysAreaQuery;
import com.kava.kbpd.upms.api.model.request.SysAreaRequest;
import com.kava.kbpd.upms.api.model.response.SysAreaListResponse;
import com.kava.kbpd.upms.api.model.response.SysAreaDetailResponse;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAreaAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery;
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
    private ISysAreaAppService sysAreaAppService;
    @Resource
    private SysAreaAdapterConverter sysAreaTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAreaListResponse>> getSysAreaPage(@ModelAttribute SysAreaQuery query) {
        PagingInfo<SysAreaAppListDTO> sysAreaEntityPagingInfo = sysAreaAppService.queryAreaPage(sysAreaTriggerConverter.convertQueryDTO2QueryVal(query));
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
    public JsonResult<List<Tree<Long>>> getSysAreaTree(@ModelAttribute SysAreaQuery query) {
        SysAreaListQuery q = sysAreaTriggerConverter.convertQueryDTO2QueryVal(query);
        List<Tree<Long>> result = sysAreaAppService.selectAreaTree(q);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysAreaDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysAreaAppDetailDTO sysAreaEntity = sysAreaAppService.queryAreaById(SysAreaId.of(id));
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
        SysAreaId sysAreaId = sysAreaAppService.createArea(sysAreaTriggerConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysAreaId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysAreaRequest req) {
        req.setId(id);
        sysAreaAppService.updateArea(sysAreaTriggerConverter.convertRequest2UpdateCommand(req));
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
        List<SysAreaId> idList = ids.stream().map(SysAreaId::of).toList();
        sysAreaAppService.removeAreaBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}
