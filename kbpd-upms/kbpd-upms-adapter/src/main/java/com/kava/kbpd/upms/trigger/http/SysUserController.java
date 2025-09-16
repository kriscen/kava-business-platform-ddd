package com.kava.kbpd.upms.trigger.http;

import com.kava.kbpd.common.core.base.JsonResult;
import com.kava.kbpd.common.core.base.PagingInfo;
import com.kava.kbpd.upms.api.model.query.SysUserQuery;
import com.kava.kbpd.upms.api.model.request.SysUserRequest;
import com.kava.kbpd.upms.api.model.response.SysUserListResponse;
import com.kava.kbpd.upms.api.model.response.SysUserResponse;
import com.kava.kbpd.upms.domain.user.model.entity.SysUserEntity;
import com.kava.kbpd.upms.domain.user.model.valobj.SysUserId;
import com.kava.kbpd.upms.domain.user.model.valobj.SysUserListQuery;
import com.kava.kbpd.upms.domain.user.service.ISysUserService;
import com.kava.kbpd.upms.trigger.converter.SysUserTriggerConverter;
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
    private ISysUserService sysUserService;
    @Resource
    private SysUserTriggerConverter sysUserTriggerConverter;

    /**
     * 分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public JsonResult<PagingInfo<SysUserListResponse>> getSysAreaPage(SysUserQuery query) {
        SysUserListQuery q = sysUserTriggerConverter.convertQueryDTO2QueryVal(query);
        PagingInfo<SysUserEntity> pagingInfo = sysUserService.queryPage(q);
        PagingInfo<SysUserListResponse> result = PagingInfo.toResponse(pagingInfo.getList().stream().
                        map(sysUserTriggerConverter::convertEntity2List).toList(),
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
    public JsonResult<SysUserResponse> getDetails(Long id) {
        SysUserEntity sysUser = sysUserService.queryById(SysUserId.builder()
                .id(id)
                .build());
        return JsonResult.buildSuccess(sysUserTriggerConverter.convertEntity2Detail(sysUser));
    }

    /**
     * 新增
     *
     * @param req 新增请求
     * @return R
     */
    @PostMapping
    public JsonResult<Long> save(@RequestBody SysUserRequest req) {
        SysUserId id = sysUserService.create(sysUserTriggerConverter.convertRequest2Entity(req));
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
        return JsonResult.buildSuccess(sysUserService.update(sysUserTriggerConverter.convertRequest2Entity(req)));
    }

    /**
     * 通过id删除
     *
     * @param ids id列表
     * @return R
     */
    @DeleteMapping
    public JsonResult<Boolean> removeById(@RequestBody List<Long> ids) {
        List<SysUserId> idList = ids.stream().map(t->SysUserId.builder().id(t).build()).toList();
        return JsonResult.buildSuccess(sysUserService.removeBatchByIds(idList));
    }

}