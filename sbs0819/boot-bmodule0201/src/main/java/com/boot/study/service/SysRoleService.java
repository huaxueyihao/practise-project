package com.boot.study.service;


import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysRole;

import java.util.List;

public interface SysRoleService {


    /**
     * 新增
     * @param sysRole
     */
    void add(SysRole sysRole);

    /**
     * 详情
     * @param id
     * @return
     */
    SysRole detail(Long id);

    /**
     * 修改
     * @param sysRole
     */
    void update(SysRole sysRole);

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
    PageResult<SysRole> pageList(PageParam<SysRole> pageParam);

    /**
     * 增加权限
     * @param roleId
     * @param permIdList
     * @return
     */
    int addPermission(Long roleId, List<Long> permIdList);

    /**
     * 删除权限
     * @param userId
     * @param permIdList
     * @return
     */
    int removePermission(Long userId, List<Long> permIdList);
}
