package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysOauthClientAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysOauthClientAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysOauthClientRequest;
import com.kava.kbpd.upms.api.model.response.SysOauthClientDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysOauthClientListResponse;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysOauthClientAppListDTO;
import com.kava.kbpd.upms.application.service.ISysOauthClientAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysOauthClientId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/oauth-client-details/")
public class SysOauthClientController {
    @Resource
    private ISysOauthClientAppService appService;
    @Resource
    private SysOauthClientAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysOauthClientListResponse>> getSysOauthClientPage(@ModelAttribute SysOauthClientAdapterListQuery query) {
        PagingInfo<SysOauthClientAppListDTO> sysOauthClientEntityPagingInfo = appService.queryOauthClientPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysOauthClientListResponse> result = PagingInfo.toResponse(sysOauthClientEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysOauthClientEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysOauthClientDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysOauthClientAppDetailDTO sysOauthClientEntity = appService.queryOauthClientById(SysOauthClientId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysOauthClientEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysOauthClientRequest req) {
        SysOauthClientId sysOauthClientId = appService.createOauthClient(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysOauthClientId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysOauthClientRequest req) {
        req.setId(id);
        appService.updateOauthClient(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysOauthClientId> idList = ids.stream().map(SysOauthClientId::of).toList();
        appService.removeOauthClientBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}