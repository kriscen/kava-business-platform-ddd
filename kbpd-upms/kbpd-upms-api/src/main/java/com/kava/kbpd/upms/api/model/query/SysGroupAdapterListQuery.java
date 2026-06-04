package com.kava.kbpd.upms.api.model.query;

import com.kava.kbpd.common.core.base.AdapterBaseListQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: 分组管理 query对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysGroupAdapterListQuery extends AdapterBaseListQuery {

    /**
     * 分组ID
     */
    private Long groupId;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 父级分组id
     */
    private Long parentId;

}
