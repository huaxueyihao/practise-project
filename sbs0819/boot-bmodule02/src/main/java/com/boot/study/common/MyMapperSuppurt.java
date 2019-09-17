package com.boot.study.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * mapper接口的父类
 * @param <T>
 */
public interface MyMapperSuppurt<T> extends Mapper<T>,MySqlMapper<T> {
}
