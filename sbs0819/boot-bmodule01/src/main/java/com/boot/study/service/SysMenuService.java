package com.boot.study.service;

import com.boot.study.common.PageResult;
import com.boot.study.model.SysMenu;
import com.boot.study.model.SysUser;

public interface SysMenuService {

    PageResult<SysMenu> pageList(Integer page, Integer limit,String menuName);

    void addUser(SysMenu sysMenu);

    SysUser detail(Long id);

    void update(SysMenu sysMenu);

    void remove(Long id);

    void batchRemove(Long[] ids);
}
