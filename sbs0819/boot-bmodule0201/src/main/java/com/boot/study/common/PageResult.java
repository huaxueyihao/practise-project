package com.boot.study.common;

import lombok.Data;

import java.util.List;

/**
 * service 层分页结果的数据封装对象
 * @param <T>
 */
@Data
public class PageResult<T> {

    /**
     * 数量
     */
    private long count;

    /**
     * 结果集
     */
    private List<T> dataList;


}
