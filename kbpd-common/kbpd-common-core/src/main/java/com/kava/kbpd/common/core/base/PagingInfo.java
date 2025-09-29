package com.kava.kbpd.common.core.base;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kris
 * @date 2025/3/25
 * @description: 分页查询结果
 */
@Data
public class PagingInfo<T> implements Serializable {

    /**
     * 页码
     */
    private int pageNo = 1;
    /**
     * 每页条数
     */
    private int pageSize = 10;
    /**
     * 总条数
     */
    private Long total;

    private List<T> list;

    public PagingInfo() {

    }
    public Integer getStartPos(){
        return (pageNo - 1) * pageSize;
    }

    public PagingInfo(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public static <T> PagingInfo<T> toResponse(List<T> data, PagingInfo page) {
        PagingInfo<T> pagingObj = new PagingInfo<>();
        pagingObj.setTotal(page.getTotal());
        pagingObj.setList(data);
        pagingObj.setPageNo(page.getPageNo());
        pagingObj.setPageSize(page.getPageSize());
        return pagingObj;
    }

    public static <T> PagingInfo<T> toResponse(List<T> data, Long total, Integer currentPageNo, Integer currentPageSize) {
        PagingInfo<T> pagingObj = new PagingInfo<>();
        pagingObj.setTotal(total);
        pagingObj.setList(data);
        pagingObj.setPageNo(currentPageNo);
        pagingObj.setPageSize(currentPageSize);
        return pagingObj;
    }

    public static <T> PagingInfo<T> toResponse(List<T> data, Long total, Long currentPageNo, Long currentPageSize) {
        PagingInfo<T> pagingObj = new PagingInfo<>();
        pagingObj.setTotal(total);
        pagingObj.setList(data);
        pagingObj.setPageNo(currentPageNo.intValue());
        pagingObj.setPageSize(currentPageSize.intValue());
        return pagingObj;
    }

}
