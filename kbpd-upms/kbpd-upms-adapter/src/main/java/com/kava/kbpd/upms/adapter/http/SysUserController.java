package com.kava.kbpd.upms.adapter.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.common.core.model.valobj.SysUserId;
import com.kava.kbpd.upms.adapter.converter.SysUserAdapterConverter;
import com.kava.kbpd.upms.api.model.query.SysUserQuery;
import com.kava.kbpd.upms.api.model.request.SysUserRequest;
import com.kava.kbpd.upms.api.model.response.SysUserDetailResponse;
import com.kava.kbpd.upms.api.model.response.SysUserListResponse;
import com.kava.kbpd.upms.application.model.dto.SysUserAppDetailDTO;
import com.kava.kbpd.upms.application.model.dto.SysUserAppListDTO;
import com.kava.kbpd.upms.application.service.ISysUserAppService;
import com.kava.kbpd.upms.domain.model.valobj.SysUserListQuery;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sys/user/")
public class SysUserController {
    @Resource
    private ISysUserAppService sysUserAppService;
    @Resource
    private SysUserAdapterConverter sysUserTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysUserListResponse>> getSysAreaPage(@ModelAttribute SysUserQuery query) {
        SysUserListQuery q = sysUserTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysUserAppListDTO> queryUserPage = sysUserAppService.queryUserPage(q);
        PagingInfo<SysUserListResponse> result = PagingInfo.toResponse(queryUserPage.getList().stream().
                        map(sysUserTriggerConverter::convertDTO2List).toList(),
                queryUserPage);
        return JsonResult.buildSuccess(result);
    }


    /**
     * 获取详细信息
     *
     * @param id 查询id
     * @return 明细
     */
    @GetMapping("/{id}}")
    public JsonResult<SysUserDetailResponse> getDetails(@PathVariable("id") Long id) {
        SysUserAppDetailDTO sysUser = sysUserAppService.queryUserById(SysUserId.of(id));
        return JsonResult.buildSuccess(sysUserTriggerConverter.convertDetailDTO2DetailResp(sysUser));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysUserRequest req) {
        SysUserId id = sysUserAppService.createUser(sysUserTriggerConverter.convertRequest2CreateCommand(req));
        return JsonResult.buildSuccess(id.getId());
    }

    /**
     * 修改
     *
     * @param req 修改请求
     * @return R
     */
    @PutMapping
    public JsonResult<Boolean> updateById(@RequestBody SysUserRequest req) {
        sysUserAppService.updateUser(sysUserTriggerConverter.convertRequest2UpdateCommand(req));
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
        List<SysUserId> idList = ids.stream().map(t->SysUserId.of(t)).toList();
        sysUserAppService.removeUserBatchByIds(idList);
        return JsonResult.buildSuccess();
    }

}