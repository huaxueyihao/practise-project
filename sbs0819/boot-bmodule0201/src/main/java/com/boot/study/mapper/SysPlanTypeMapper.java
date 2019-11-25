package com.boot.study.mapper;


import com.boot.study.common.MyMapperSupport;
import com.boot.study.model.SysPlanType;
import org.apache.ibatis.annotations.Param;

public interface SysPlanTypeMapper extends MyMapperSupport<SysPlanType> {

    int countByTypeName(@Param("typeName") String typeName);
}
