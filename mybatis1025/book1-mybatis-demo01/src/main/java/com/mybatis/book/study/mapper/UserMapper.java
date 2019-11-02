package com.mybatis.book.study.mapper;

import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysRoleExtend;
import com.mybatis.book.study.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    /**
     * 通过id查询用户
     *
     * @param id
     * @return
     */
    SysUser selectById(Long id);

    /**
     * 查询全部用户
     *
     * @return
     */
    List<SysUser> selectAll();

    /**
     * 根据用户id获取角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> selectRolesByUserId(Long userId);


    /**
     * 根据用户id获取角色信息
     * 返回带userName列
     *
     * @param userId
     * @return
     */
    List<SysRoleExtend> selectRolesByUserIdExtend(Long userId);


    /**
     * 根据用户id获取角色信息
     * 返回带SysUser多个列
     *
     * @param userId
     * @return
     */
    List<SysRole> selectRolesByUserIdWithSysUser(Long userId);

    /**
     * 新增用户
     *
     * @param sysUser
     * @return
     */
    int insert(SysUser sysUser);

    /**
     * 新增用户-使用useGenerateKeys方式
     *
     * @param sysUser
     * @return
     */
    int insert2(SysUser sysUser);

    /**
     * 新增用户-使用selectKey方式
     *
     * @param sysUser
     * @return
     */
    int insert3(SysUser sysUser);

    /**
     * 根据主键个更新
     *
     * @param sysUser
     * @return
     */
    int updateById(SysUser sysUser);

    /**
     * 通过主键删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据用户id和角色enabled状态获取角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> selectRolesByUserIdAndRoleEnabled(@Param("userId") Long userId, @Param("enabled") Integer enabled);


    /**
     * 根据用户id和角色enabled状态获取角色信息
     *
     * @param user
     * @param role
     * @return
     */
    List<SysRole> selectRolesByUserAndRole(@Param("user") SysUser user, @Param("role") SysRole role);


    /**
     * 根据动态条件查询用户信息
     *
     * @param sysUser
     * @return
     */
    List<SysUser> selectByUser(SysUser sysUser);


    /**
     * 动态修改
     *
     * @param sysUser
     * @return
     */
    int updateByIdSelective(SysUser sysUser);


    /**
     * 动态插入
     *
     * @param sysUser
     * @return
     */
    int insertSelective(SysUser sysUser);


    /**
     * 根据用户id或用户名查询
     *
     * @param sysUser
     * @return
     */
    SysUser selectByIdOrUserName(SysUser sysUser);

    /**
     * 根据用户id集合查询
     *
     * @param idList
     * @return
     */
    List<SysUser> selectByIdList(List<Long> idList);


    /**
     * 根据用户id数组查询
     *
     * @param idList
     * @return
     */
    List<SysUser> selectByIdList(Long[] idList);


    /**
     * 批量插入用户信息
     *
     * @param userList
     * @return
     */
    int insertList(List<SysUser> userList);


    /**
     * 通过Map更新列
     *
     * @param map
     * @return
     */
    int updateByMap(Map<String, Object> map);


    /**
     * 根据用户id获取用户信息和用户的角色信息：根据(字段.字段：role.roleName)自动映射
     *
     * @param id
     * @return
     */
    SysUser selectUserAndRoleById(Long id);


    /**
     * 根据用户id获取用户信息和用户的角色信息
     *
     * @param id
     * @return
     */
    SysUser selectUserAndRoleById2(Long id);


    /**
     * 根据用户id获取用户信息和用户的角色信息：association
     *
     * @param id
     * @return
     */
    SysUser selectUserAndRoleById3(Long id);


    /**
     * 根据id获取用户信息和角色信息：association嵌套查询
     *
     * @param id
     * @return
     */
    SysUser selectUserAndRoleByIdSelect(Long id);


    /**
     * 获取所有的用户以及对应的所有角色
     *
     * @return
     */
    List<SysUser> selectAllUserAndRoles();


    /**
     * 获取所有的用户以及对应的所有角色,以及角色拥有的所有权限
     *
     * @return
     */
    List<SysUser> selectAllUserAndRolesAndPrivileges();

    /**
     * 通过嵌套查询获取指定用户的信息以及用户的角色和权限信息
     *
     * @param id
     * @return
     */
    SysUser selectAllUserAndRolesSelect(Long id);


    /**
     * 使用存储过程查询用户信息
     *
     * @param user
     */
    void selectUserById(SysUser user);

    /**
     * 使用存储过程分页查询
     *
     * @param params
     * @return
     */
    List<SysUser> selectUserPage(Map<String, Object> params);


    /**
     * 存储过程进行新增
     *
     * @param user
     * @param roleIds
     * @return
     */
    int insertUserAndRoles(@Param("user") SysUser user, @Param("roleIds") String roleIds);

    /**
     * 根据用户Id删除用户和用户的角色信息
     *
     * @param id
     * @return
     */
    int deleteUserById(Long id);


}
