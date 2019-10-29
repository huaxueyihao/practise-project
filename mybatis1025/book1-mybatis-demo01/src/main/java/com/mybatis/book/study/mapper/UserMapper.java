package com.mybatis.book.study.mapper;

import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysRoleExtend;
import com.mybatis.book.study.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

}
