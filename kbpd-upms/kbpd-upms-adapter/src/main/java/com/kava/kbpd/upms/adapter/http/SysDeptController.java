package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysDeptAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysDeptAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysDeptRequest;
import com.kava.kbpd.upms.api.model.response.SysDeptDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysDeptListResponse;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysDeptAppListDTO;
import com.kava.kbpd.upms.application.service.ISysDeptAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/dept/")
public class SysDeptController {
    @Resource
    private ISysDeptAppService appService;
    @Resource
    private SysDeptAdapterConverter adapterConverter;

    /**
     * 分页查询
     *
     * @param query 行政区划
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysDeptListResponse>> getSysDeptPage(@ModelAttribute SysDeptAdapterListQuery query) {
        PagingInfo<SysDeptAppListDTO> sysDeptEntityPagingInfo = appService.queryDeptPage(adapterConverter.convertQueryDTO2QueryVal(query));
        PagingInfo<SysDeptListResponse> result = PagingInfo.toResponse(sysDeptEntityPagingInfo.getList().stream().
                        map(adapterConverter::convertEntity2List).toList(),
                sysDeptEntityPagingInfo);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取详细信息
     *
     * @param id id
     * @return 明细
     */
    @GetMapping("/{id}")
    public JsonResult<SysDeptDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysDeptAppDetailDTO sysDeptEntity = appService.queryDeptById(SysDeptId.of(id));
        return JsonResult.buildSuccess(adapterConverter.convertEntity2Detail(sysDeptEntity));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysDeptRequest req) {
        SysDeptId sysDeptId = appService.createDept(adapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(sysDeptId.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping("/{id}")
    public JsonResult<Void> updateById(@PathVariable("id") Long id,@RequestBody SysDeptRequest req) {
        req.setId(id);
        appService.updateDept(adapterConverter.convertRequest2UpdateCommand(req));
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
        List<SysDeptId> idList = ids.stream().map(SysDeptId::of).toList();
        appService.removeDeptBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}