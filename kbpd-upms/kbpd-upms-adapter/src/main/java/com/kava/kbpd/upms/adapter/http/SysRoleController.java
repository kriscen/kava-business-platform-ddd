package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.adapter.converter.SysRoleAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysRoleAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysRoleRequest;
import com.kava.kbpd.upms.api.model.response.SysRoleDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysRoleListResponse;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.application.service.ISysRoleAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
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
    private ISysRoleAppService sysRoleAppService;
    @Resource
    private SysRoleAdapterConverter sysRoleTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysRoleListResponse>> getSysAreaPage(@ModelAttribute SysRoleAdapterListQuery query) {
        SysRoleListQuery q = sysRoleTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysRoleAppListDTO> queryRolePage = sysRoleAppService.queryRolePage(q);
        PagingInfo<SysRoleListResponse> result = PagingInfo.toResponse(queryRolePage.getList().stream().
                        map(sysRoleTriggerConverter::convertDTO2List).toList(),
                queryRolePage);
        return JsonResult.buildSuccess(result);
    }


    /**
     * 获取详细信息
     *
     * @param id 查询id
     * @return 明细
     */
    @GetMapping("/{id}}")
    public JsonResult<SysRoleDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysRoleAppDetailDTO sysRole = sysRoleAppService.queryRoleById(SysRoleId.of(id));
        return JsonResult.buildSuccess(sysRoleTriggerConverter.convertDetailDTO2DetailResp(sysRole));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysRoleRequest req) {
        SysRoleId id = sysRoleAppService.createRole(sysRoleTriggerConverter.convertRequest2CreateCommand(req));
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
        sysRoleAppService.updateRole(sysRoleTriggerConverter.convertRequest2UpdateCommand(req));
        return JsonResult.buildSuccess();
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysRoleId> idList = ids.stream().map(t->SysRoleId.of(t)).toList();
        sysRoleAppService.removeRoleBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}