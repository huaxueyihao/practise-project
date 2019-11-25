package com.boot.study.service;


import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysPermission;

public interface SysPermissionService {


    /**
     * 新增
     * @param sysPermission
     */
    void add(SysPermission sysPermission);

    /**
     * 详情
     * @param id
     * @return
     */
    SysPermission detail(Long id);

    /**
     * 修改
     * @param sysPermission
     */
    void update(SysPermission sysPermission);

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
    PageResult<SysPermission> pageList(PageParam<SysPermission> pageParam);

}
