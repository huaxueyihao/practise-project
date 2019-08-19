package com.boot.study.mapper;

import com.boot.study.model.SysUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper {

    @Select("select t.* from sys_user t")
    List<SysUser> selectAll();

}
