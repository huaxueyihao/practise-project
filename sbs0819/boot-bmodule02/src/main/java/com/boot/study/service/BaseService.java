package com.boot.study.service;

import com.boot.study.common.PageParam;

public interface BaseService<T> {

    void save(T t);

    void update(T t);

    void delete(Object id);

    void pageList(PageParam<T> param);
}
