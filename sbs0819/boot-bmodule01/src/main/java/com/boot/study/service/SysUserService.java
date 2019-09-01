package com.boot.study.service;

import com.boot.study.common.PageResult;
import com.boot.study.model.SysUser;

import java.util.List;

public interface SysUserService {
    List<SysUser> getAllUser();

    PageResult<SysUser> pageList(Integer page, Integer limit);

    void addUser(SysUser sysUser);

    SysUser detail(Long id);

    void update(SysUser sysUser);

    void remove(Long id);

    void batchRemove(Long[] ids);
}
