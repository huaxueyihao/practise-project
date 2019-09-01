package com.boot.study.mapper;

import com.boot.study.common.MyMapperSuppurt;
import com.boot.study.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysUserMapper extends MyMapperSuppurt<SysUser> {

//    @Select("select count(0) from SYSTEM_USER t where t.user_name = #{userName} ")
//    int countByUserName(@Param("userName") String userName);

}
