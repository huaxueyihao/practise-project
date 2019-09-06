package com.boot.study.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private long count;

    private List<T> dataList;


}
