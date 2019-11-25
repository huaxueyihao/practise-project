package com.boot.study.mapper;

import com.boot.study.common.MyMapperSupport;
import com.boot.study.model.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.security.Permission;
import java.util.List;

public interface SysPermissionMapper extends MyMapperSupport<SysPermission> {

    @Select("select count(0) from sys_permission p where p.perm_name = #{permName} ")
    int selectCountByPermName(String permName);

    @Select("select distinct p.id,p.perm_name permName,p.perm_desc permDesc,p.remark from sys_permission p left join sys_role_perm rp on p.id = rp.perm_id where rp.role_id = #{roleId};")
    List<SysPermission> selectPermissionListByRoleId(@Param("roleId") Long roleId);

    @Select("select distinct p.id,p.perm_name permName,p.perm_desc permDesc,p.remark from sys_user_role ur left join  sys_role_perm rp on ur.role_id = rp.role_id left join sys_permission p on rp.perm_id = p.id  where ur.user_id = #{userId};")
    List<SysPermission> selectPermissionListByUserId(@Param("userId") Long userId);

}
