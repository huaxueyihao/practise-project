package com.boot.study.mapper;


import com.boot.study.model.SysUser;
import com.boot.study.common.MyMapperSuppurt;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends MyMapperSuppurt<SysUser> {

    SysUser selectByUserName(@Param("userName") String userName);

}
