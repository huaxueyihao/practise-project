package com.boot.study.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapperSuppurt<T> extends Mapper<T>,MySqlMapper<T> {
}
