package com.boot.study.service;

import com.boot.study.bean.MenuTreeDto;
import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.common.ZTreeDto;
import com.boot.study.model.SysMenu;

import java.util.List;

public interface SysMenuService {

    PageResult<SysMenu> pageList(Integer page, Integer limit, String menuName);

    void addUser(SysMenu sysMenu);

    SysMenu detail(Long id);

    void update(SysMenu sysMenu);

    void remove(Long id);

    void batchRemove(Long[] ids);

    List<TreeDto> menuTree(Long parentId);

    List<ZTreeDto> menuZTree(Long parentId);

    List<MenuTreeDto> indexMenuTree(Long parentId);
}
