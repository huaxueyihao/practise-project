package tk.mybatis.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MapperSupport<T> extends Mapper<T>, MySqlMapper<T> {
}
