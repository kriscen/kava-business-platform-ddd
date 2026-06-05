package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysGroupAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysGroupAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysGroupRequest;
import com.kava.kbpd.upms.api.model.response.SysGroupDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysGroupListResponse;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysGroupAppListDTO;
import com.kava.kbpd.upms.application.service.ISysGroupAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysGroupId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/group")
public class SysGroupController {
    @Resource
    private ISysGroupAppService appService;
    @Resource
    private SysGroupAdapterConverter adapterConverter;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysGroupListResponse>> getSysGroupPage(@ModelAttribute SysGroupAdapterListQuery query) {
        PagingInfo<SysGroupAppListDTO> sysGroupEntityPagingInfo = appService.queryGroupPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysGroupListResponse> result = PagingInfo.toResponse(sysGroupEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysGroupEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     */
    @GetMapping("/{id}")
    public JsonResult<SysGroupDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysGroupAppDetailDTO sysGroupEntity = appService.queryGroupById(SysGroupId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysGroupEntity));
    }

    /**
     * 新增
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysGroupRequest req) {
        SysGroupId sysGroupId = appService.createGroup(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysGroupId.getId());
    }

    /**
     * 修改
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysGroupRequest req) {
        req.setId(id);
        appService.updateGroup(adapterConverter.convertRequest2UpdateCommand(req));
        return JsonResult.buildSuccess();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping
    public JsonResult<Void> removeById(@RequestBody List<Long> ids) {
        List<SysGroupId> idList = ids.stream().map(SysGroupId::of).toList();
        appService.removeGroupBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

    @GetMapping("/tree")
    public JsonResult<List<SysGroupListResponse>> getGroupTree() {
        List<SysGroupAppListDTO> tree = appService.queryGroupTree();
        List<SysGroupListResponse> result = tree.stream().map(adapterConverter::convertEntity2List).toList();
        return JsonResult.buildSuccess(result);
    }

}
