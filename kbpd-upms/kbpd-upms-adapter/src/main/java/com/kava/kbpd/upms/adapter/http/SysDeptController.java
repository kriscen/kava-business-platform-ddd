package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysDeptQuery;
import com.kava.kbpd.upms.api.model.request.SysDeptRequest;
import com.kava.kbpd.upms.api.model.response.SysDeptListResponse;
import com.kava.kbpd.upms.api.model.response.SysDeptResponse;
import com.kava.kbpd.upms.domain.model.entity.SysDeptEntity;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptId;
import com.kava.kbpd.upms.domain.model.valobj.SysDeptListQuery;
import com.kava.kbpd.upms.domain.service.ISysDeptService;
import com.kava.kbpd.upms.adapter.converter.SysDeptAdapterConverter;
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
    private ISysDeptService sysDeptService;
    @Resource
    private SysDeptAdapterConverter sysDeptTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysDeptListResponse>> getSysDeptPage(SysDeptQuery query) {
        SysDeptListQuery q = sysDeptTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysDeptEntity> pagingInfo = sysDeptService.queryPage(q);
        PagingInfo<SysDeptListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysDeptTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysDeptResponse> getDetails(Long id) {
        SysDeptEntity sysDept = sysDeptService.queryById(SysDeptId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysDeptTriggerConverter.convertEntity2Detail(sysDept));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysDeptRequest req) {
        SysDeptId id = sysDeptService.create(sysDeptTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysDeptRequest req) {
        return JsonResult.buildSuccess(sysDeptService.update(sysDeptTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysDeptId> idList = ids.stream().map(t->SysDeptId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysDeptService.removeBatchByIds(idList));
    }
}