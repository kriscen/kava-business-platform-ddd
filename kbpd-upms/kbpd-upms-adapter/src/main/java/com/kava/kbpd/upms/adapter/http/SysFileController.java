package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysFileAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysFileAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysFileRequest;
import com.kava.kbpd.upms.api.model.response.SysFileDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysFileListResponse;
import com.kava.kbpd.upms.application.model.dto.SysFileAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysFileAppListDTO;
import com.kava.kbpd.upms.application.service.ISysFileAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysFileId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/file/")
public class SysFileController {
    @Resource
    private ISysFileAppService appService;
    @Resource
    private SysFileAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysFileListResponse>> getSysFilePage(@ModelAttribute SysFileAdapterListQuery query) {
        PagingInfo<SysFileAppListDTO> sysFileEntityPagingInfo = appService.queryFilePage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysFileListResponse> result = PagingInfo.toResponse(sysFileEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysFileEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysFileDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysFileAppDetailDTO sysFileEntity = appService.queryFileById(SysFileId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysFileEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysFileRequest req) {
        SysFileId sysFileId = appService.createFile(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysFileId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysFileRequest req) {
        req.setId(id);
        appService.updateFile(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysFileId> idList = ids.stream().map(SysFileId::of).toList();
        appService.removeFileBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}