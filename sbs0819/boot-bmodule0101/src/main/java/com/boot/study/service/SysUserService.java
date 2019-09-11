package com.boot.study.service;

import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysUser;

import java.util.List;

public interface SysUserService {
    /**
     * 用户列表
     * @return
     */
    List<SysUser> getAllUser();

    /**
     * 分页
     * @param page
     * @param limit
     * @return
     */
    PageResult<SysUser> pageList(Integer page, Integer limit);

    /**
     * 新增
     * @param sysUser
     */
    void addUser(SysUser sysUser);

    /**
     * 详情
     * @param id
     * @return
     */
    SysUser detail(Long id);

    /**
     * 修改
     * @param sysUser
     */
    void update(SysUser sysUser);

    /**
     * 删除
     * @param id
     */
    void remove(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchRemove(Long[] ids);

    /**
     * 分页
     * @param pageParam
     * @return
     */
    PageResult<SysUser> pageList(PageParam<SysUser> pageParam);

    /**
     * 登陆检验
     * @param userName
     * @param password
     * @return
     */
    boolean checkByUserNameAndPassword(String userName, String password);
}
