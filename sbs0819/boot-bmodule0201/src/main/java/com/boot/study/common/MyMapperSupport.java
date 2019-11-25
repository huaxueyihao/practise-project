package com.boot.study.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * mapper接口的父类
 * @param <T>
 */
public interface MyMapperSupport<T> extends Mapper<T>,MySqlMapper<T> {
}
