package com.boot.study.mapper;


import com.boot.study.common.MyMapperSupport;
import com.boot.study.model.SysPermission;
import com.boot.study.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends MyMapperSupport<SysUser> {

    SysUser selectByUserName(@Param("userName") String userName);

    int batchInsertUserRole(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);

    int deleteUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Select("select count(0) from sys_user_role ur where ur.user_id = #{userId} and ur.role_id = #{roleId}")
    int selectCountByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    List<SysPermission> selectPermissionListByUserId(Long userId);
}
