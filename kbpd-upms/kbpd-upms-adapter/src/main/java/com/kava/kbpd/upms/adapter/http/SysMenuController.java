package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysMenuAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysMenuAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysMenuRequest;
import com.kava.kbpd.upms.api.model.response.SysMenuDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysMenuListResponse;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysMenuAppListDTO;
import com.kava.kbpd.upms.application.service.ISysMenuAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysMenuId;
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
    private ISysMenuAppService appService;
    @Resource
    private SysMenuAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysMenuListResponse>> getSysMenuPage(@ModelAttribute SysMenuAdapterListQuery query) {
        PagingInfo<SysMenuAppListDTO> sysMenuEntityPagingInfo = appService.queryMenuPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysMenuListResponse> result = PagingInfo.toResponse(sysMenuEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysMenuEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysMenuDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysMenuAppDetailDTO sysMenuEntity = appService.queryMenuById(SysMenuId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysMenuEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysMenuRequest req) {
        SysMenuId sysMenuId = appService.createMenu(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysMenuId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysMenuRequest req) {
        req.setId(id);
        appService.updateMenu(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysMenuId> idList = ids.stream().map(SysMenuId::of).toList();
        appService.removeMenuBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}