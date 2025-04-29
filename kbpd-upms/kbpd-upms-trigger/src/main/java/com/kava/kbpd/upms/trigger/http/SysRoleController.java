package com.kava.kbpd.upms.trigger.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysRoleQuery;
import com.kava.kbpd.upms.api.model.request.SysRoleRequest;
import com.kava.kbpd.upms.api.model.response.SysRoleListResponse;
import com.kava.kbpd.upms.api.model.response.SysRoleResponse;
import com.kava.kbpd.upms.domain.permission.model.entity.SysRoleEntity;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.permission.model.valobj.SysRoleListQuery;
import com.kava.kbpd.upms.domain.permission.service.ISysRoleService;
import com.kava.kbpd.upms.trigger.converter.SysRoleTriggerConverter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/role/")
public class SysRoleController {
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private SysRoleTriggerConverter sysRoleTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysRoleListResponse>> getSysRolePage(SysRoleQuery query) {
        SysRoleListQuery q = sysRoleTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysRoleEntity> pagingInfo = sysRoleService.queryPage(q);
        PagingInfo<SysRoleListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysRoleTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysRoleResponse> getDetails(Long id) {
        SysRoleEntity sysRole = sysRoleService.queryById(SysRoleId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysRoleTriggerConverter.convertEntity2Detail(sysRole));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysRoleRequest req) {
        SysRoleId id = sysRoleService.create(sysRoleTriggerConverter.convertRequest2Entity(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysRoleRequest req) {
        return JsonResult.buildSuccess(sysRoleService.update(sysRoleTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysRoleId> idList = ids.stream().map(t->SysRoleId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysRoleService.removeBatchByIds(idList));
    }

}