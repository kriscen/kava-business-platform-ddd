package com.kava.kbpd.upms.adapter.http;

import cn.hutool.core.lang.tree.Tree;
import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysAreaAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysAreaAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysAreaRequest;
import com.kava.kbpd.upms.api.model.response.SysAreaListResponse;
import com.kava.kbpd.upms.api.model.response.SysAreaDetailResponse;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysAreaAppListDTO;
import com.kava.kbpd.upms.application.service.ISysAreaAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysAreaId;
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
    private ISysAreaAppService appService;
    @Resource
    private SysAreaAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysAreaListResponse>> getSysAreaPage(@ModelAttribute SysAreaAdapterListQuery query) {
        PagingInfo<SysAreaAppListDTO> sysAreaEntityPagingInfo = appService.queryAreaPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysAreaListResponse> result = PagingInfo.toResponse(sysAreaEntityPagingInfo.getList().stream().
                                                    map(adapterConverter::convertEntity2List).toList(),
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
    public JsonResult<List<Tree<Long>>> getSysAreaTree(@ModelAttribute SysAreaAdapterListQuery query) {
        com.kava.kbpd.upms.domain.model.valobj.SysAreaListQuery q = adapterConverter.convertQueryDTO2QueryVal(query);
        List<Tree<Long>> result = appService.selectAreaTree(q);
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
        SysAreaAppDetailDTO sysAreaEntity = appService.queryAreaById(SysAreaId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysAreaEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysAreaRequest req) {
        SysAreaId sysAreaId = appService.createArea(adapterConverter.convertRequest2CreateCommand(req));
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
        appService.updateArea(adapterConverter.convertRequest2UpdateCommand(req));
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
        appService.removeAreaBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}
