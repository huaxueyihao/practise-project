package com.boot.study.mapper;


import com.boot.study.common.MyMapperSuppurt;
import com.boot.study.model.SysPlanType;
import org.apache.ibatis.annotations.Param;

public interface SysPlanTypeMapper extends MyMapperSuppurt<SysPlanType> {

    int countByTypeName(@Param("typeName") String typeName);
}
