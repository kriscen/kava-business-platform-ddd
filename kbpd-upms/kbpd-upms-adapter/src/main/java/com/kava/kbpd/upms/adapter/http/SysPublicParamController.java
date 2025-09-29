package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysPublicParamQuery;
import com.kava.kbpd.upms.api.model.request.SysPublicParamRequest;
import com.kava.kbpd.upms.api.model.response.SysPublicParamListResponse;
import com.kava.kbpd.upms.api.model.response.SysPublicParamResponse;
import com.kava.kbpd.upms.domain.model.entity.SysPublicParamEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamId;
import com.kava.kbpd.upms.domain.model.valobj.SysPublicParamListQuery;
import com.kava.kbpd.upms.domain.service.ISysPublicParamService;
import com.kava.kbpd.upms.adapter.converter.SysPublicParamAdapterConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/public-param/")
public class SysPublicParamController {
    @Resource
    private ISysPublicParamService sysPublicParamService;
    @Resource
    private SysPublicParamAdapterConverter sysPublicParamTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysPublicParamListResponse>> getSysPublicParamPage(SysPublicParamQuery query) {
        SysPublicParamListQuery q = sysPublicParamTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysPublicParamEntity> pagingInfo = sysPublicParamService.queryPage(q);
        PagingInfo<SysPublicParamListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysPublicParamTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysPublicParamResponse> getDetails(Long id) {
        SysPublicParamEntity sysPublicParam = sysPublicParamService.queryById(SysPublicParamId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysPublicParamTriggerConverter.convertEntity2Detail(sysPublicParam));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysPublicParamRequest req) {
        SysPublicParamId id = sysPublicParamService.create(sysPublicParamTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysPublicParamRequest req) {
        return JsonResult.buildSuccess(sysPublicParamService.update(sysPublicParamTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysPublicParamId> idList = ids.stream().map(t->SysPublicParamId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysPublicParamService.removeBatchByIds(idList));
    }
}