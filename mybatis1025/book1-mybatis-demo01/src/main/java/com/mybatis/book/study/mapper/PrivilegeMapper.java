package com.mybatis.book.study.mapper;

import com.mybatis.book.study.mapper.provider.PrivilegeProvider;
import com.mybatis.book.study.model.SysPrivilege;
import org.apache.ibatis.annotations.SelectProvider;

public interface PrivilegeMapper {


    @SelectProvider(type = PrivilegeProvider.class,method = "selectById")
    SysPrivilege selectById(Long id);




}
