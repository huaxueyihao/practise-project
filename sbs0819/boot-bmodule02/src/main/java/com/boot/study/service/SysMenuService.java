package com.boot.study.service;

import com.boot.study.model.SysMenu;
import com.boot.study.bean.MenuTreeDto;
import com.boot.study.bean.MiniMenuTreeDto;
import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.common.ZTreeDto;

import java.util.List;

public interface SysMenuService {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param menuName
     * @return
     */
    PageResult<SysMenu> pageList(Integer page, Integer limit, String menuName);

    /**
     * 增加
     * @param sysMenu
     */
    void addMenu(SysMenu sysMenu);

    /**
     * 详情
     * @param id
     * @return
     */
    SysMenu detail(Long id);

    /**
     * 修改
     * @param sysMenu
     */
    void update(SysMenu sysMenu);

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
     * 菜单树
     * @param parentId
     * @return
     */
    List<TreeDto> menuTree(Long parentId);

    /**
     * 菜单树
     * @param parentId
     * @return
     */
    List<ZTreeDto> menuZTree(Long parentId);

    /**
     * 菜单树
     * @param parentId
     * @return
     */
    List<MenuTreeDto> indexMenuTree(Long parentId);

    /**
     * 菜单树
     * @param parentId
     * @return
     */
    List<MiniMenuTreeDto> miniMenuZTree(Long parentId);

    /**
     * 菜单列表
     * @param parentId
     * @return
     */
    List<SysMenu> selectByParentId(Long parentId);
}
