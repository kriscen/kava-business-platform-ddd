package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysFileGroupAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysFileGroupAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysFileGroupRequest;
import com.kava.kbpd.upms.api.model.response.SysFileGroupDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysFileGroupListResponse;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileGroupAppListDTO;
import com.kava.kbpd.upms.application.service.ISysFileGroupAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysFileGroupId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/FileGroup-group/")
public class SysFileGroupController {
    @Resource
    private ISysFileGroupAppService appService;
    @Resource
    private SysFileGroupAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysFileGroupListResponse>> getSysFileGroupPage(@ModelAttribute SysFileGroupAdapterListQuery query) {
        PagingInfo<SysFileGroupAppListDTO> sysFileGroupEntityPagingInfo = appService.queryFileGroupPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysFileGroupListResponse> result = PagingInfo.toResponse(sysFileGroupEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysFileGroupEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysFileGroupDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysFileGroupAppDetailDTO sysFileGroupEntity = appService.queryFileGroupById(SysFileGroupId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysFileGroupEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysFileGroupRequest req) {
        SysFileGroupId sysFileGroupId = appService.createFileGroup(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysFileGroupId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysFileGroupRequest req) {
        req.setId(id);
        appService.updateFileGroup(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysFileGroupId> idList = ids.stream().map(SysFileGroupId::of).toList();
        appService.removeFileGroupBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}