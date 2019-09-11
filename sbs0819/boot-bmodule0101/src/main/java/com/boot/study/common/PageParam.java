package com.boot.study.common;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.session.RowBounds;

/**
 * 分页查询时的参数对象类
 * @param <T>
 */
@Data
@ToString
public class PageParam<T> {

    /**
     * 每页的大小
     */
    private int limit;

    /**
     * 页码
     */
    private int page;

    /**
     * 查询的过滤条件
     */
    private T condition;


    public RowBounds getRowBounds() {
        page = page < 1 ? 1 : page;
        limit = limit <= 0 ? 10 : limit;
        int offset = (page - 1) * limit;
        return new RowBounds(offset, limit);
    }


}
