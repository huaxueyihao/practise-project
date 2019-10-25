package com.mybatis.book.study.mapper;

import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysRoleExtend;
import com.mybatis.book.study.model.SysUser;

import java.util.List;

public interface UserMapper {

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    SysUser selectById(Long id);

    /**
     * 查询全部用户
     * @return
     */
    List<SysUser> selectAll();

    /**
     * 根据用户id获取角色信息
     * @param userId
     * @return
     */
    List<SysRole> selectRolesByUserId(Long userId);


    /**
     * 根据用户id获取角色信息
     * 返回带userName列
     * @param userId
     * @return
     */
    List<SysRoleExtend>  selectRolesByUserIdExtend(Long userId);


    /**
     * 根据用户id获取角色信息
     * 返回带SysUser多个列
     * @param userId
     * @return
     */
    List<SysRole>  selectRolesByUserIdWithSysUser(Long userId);

}
