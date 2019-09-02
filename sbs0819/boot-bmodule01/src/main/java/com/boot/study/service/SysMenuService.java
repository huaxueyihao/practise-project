package com.boot.study.service;

import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.model.SysMenu;
import com.boot.study.model.SysUser;

import java.util.List;

public interface SysMenuService {

    PageResult<SysMenu> pageList(Integer page, Integer limit,String menuName);

    void addUser(SysMenu sysMenu);

    SysUser detail(Long id);

    void update(SysMenu sysMenu);

    void remove(Long id);

    void batchRemove(Long[] ids);

    List<TreeDto> menuTree(Long parentId);
}
