package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.UserContext;
import com.kava.kbpd.common.core.model.valobj.SysTenantId;
import com.kava.kbpd.upms.adapter.converter.SysRoleAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysRoleAdapterListQuery;
import com.kava.kbpd.upms.api.model.request.SysRoleRequest;
import com.kava.kbpd.upms.api.model.response.SysRoleDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysRoleDropdownResponse;
import com.kava.kbpd.upms.api.model.response.SysRoleListResponse;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysRoleAppListDTO;
import com.kava.kbpd.upms.application.service.ISysRoleAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleId;
import com.kava.kbpd.upms.domain.model.valobj.SysRoleListQuery;
import com.kava.kbpd.common.security.context.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final ISysRoleAppService sysRoleAppService;
    private final SysRoleAdapterConverter sysRoleAdapterConverter;

    @GetMapping("/page")
    public JsonResult<PagingInfo<SysRoleListResponse>> getSysRolePage(@ModelAttribute SysRoleAdapterListQuery query) {
        SysRoleListQuery q = sysRoleAdapterConverter.convertQueryDTO2QueryVal(query);
        UserContext ctx = UserContextHolder.get();
        if (ctx != null && ctx.getTenantId() != null) {
            q = SysRoleListQuery.builder()
                    .queryParam(q.getQueryParam())
                    .roleName(q.getRoleName())
                    .roleCode(q.getRoleCode())
                    .tenantId(SysTenantId.of(ctx.getTenantId()))
                    .build();
        }
        PagingInfo<SysRoleAppListDTO> queryRolePage = sysRoleAppService.queryRolePage(q);
        PagingInfo<SysRoleListResponse> result = PagingInfo.toResponse(queryRolePage.getList().stream()
                        .map(sysRoleAdapterConverter::convertDTO2List).toList(),
                queryRolePage);
        return JsonResult.buildSuccess(result);
    }

    @GetMapping("/{id}")
    public JsonResult<SysRoleDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysRoleAppDetailDTO sysRole = sysRoleAppService.queryRoleById(SysRoleId.of(id));
        return JsonResult.buildSuccess(sysRoleAdapterConverter.convertDetailDTO2DetailResp(sysRole));
    }

    @PostMapping
    public JsonResult<Long> save(@RequestBody SysRoleRequest req) {
        SysRoleId id = sysRoleAppService.createRole(sysRoleAdapterConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(id.getId());
    }

    @PutMapping("/{id}")
    public JsonResult<Boolean> updateById(@PathVariable("id") Long id, @RequestBody SysRoleRequest req) {
        req.setId(id);
        sysRoleAppService.updateRole(sysRoleAdapterConverter.convertRequest2UpdateCommand(req));
        return JsonResult.buildSuccess();
    }

    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysRoleId> idList = ids.stream().map(SysRoleId::of).toList();
        sysRoleAppService.removeRoleBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

    @GetMapping("/dropdown")
    public JsonResult<List<SysRoleDropdownResponse>> getRoleDropdown() {
        UserContext ctx = UserContextHolder.get();
        SysTenantId tenantId = (ctx != null && ctx.getTenantId() != null) ? SysTenantId.of(ctx.getTenantId()) : null;
        List<SysRoleAppListDTO> roles = sysRoleAppService.queryRoleDropdown(tenantId);
        List<SysRoleDropdownResponse> result = roles.stream()
                .map(dto -> {
                    SysRoleDropdownResponse resp = new SysRoleDropdownResponse();
                    resp.setId(dto.getId());
                    resp.setRoleName(dto.getRoleName());
                    resp.setRoleCode(dto.getRoleCode());
                    return resp;
                }).toList();
        return JsonResult.buildSuccess(result);
    }

}
