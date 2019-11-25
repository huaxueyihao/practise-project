package com.boot.study.mapper;

import com.boot.study.common.MyMapperSupport;
import com.boot.study.model.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysRoleMapper extends MyMapperSupport<SysRole> {


    @Select("select count(0) from sys_role r where r.role_name = #{roleName} ")
    int selectCountByRoleName(@Param("roleName") String roleName);


    List<SysRole> selectRoleListByUserId(@Param("userId") Long userId);

    @Select("select count(*) from sys_role_perm rp  where  rp.role_id = #{roleId} and rp.perm_id = #{permId};")
    int selectCountByRoleIdAndPermId(@Param("roleId") Long roleId, @Param("permId") Long permId);


    @Insert("insert into sys_role_perm (role_id, perm_id) values (#{roleId} ,#{permId} );")
    int insertRolePerm(@Param("roleId") Long roleId, @Param("permId") Long permId);

    @Delete("delete from sys_role_perm where role_id = #{roleId} and perm_id = #{permId} ")
    int deleteRolePerm(@Param("roleId") Long roleId, @Param("permId") Long permId);
}
