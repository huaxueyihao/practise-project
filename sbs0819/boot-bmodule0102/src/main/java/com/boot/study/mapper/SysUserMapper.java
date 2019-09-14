package com.boot.study.mapper;

import com.boot.study.common.MyMapperSuppurt;
import com.boot.study.model.SysUser;

public interface SysUserMapper extends MyMapperSuppurt<SysUser> {

//    @Select("select count(0) from SYSTEM_USER t where t.user_name = #{userName} ")
//    int countByUserName(@Param("userName") String userName);

}
