package com.boot.study.util;

import com.boot.study.bean.MenuTreeDto;
import com.boot.study.common.TreeDto;
import com.boot.study.common.ZTreeDto;
import com.boot.study.model.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeUtil {

    public static List<TreeDto> convert(List<SysMenu> menuList) {
        if (menuList != null && menuList.size() > 0) {
            return menuList.stream().map(menu -> TreeDto.builder().id(menu.getId()).parentId(menu.getParentId()).title(menu.getMenuName()).build()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }


    public static List<ZTreeDto> convertZtree(List<SysMenu> menuList) {
        if (menuList != null && menuList.size() > 0) {
            return menuList.stream().map(menu -> ZTreeDto.builder().id(menu.getId()).parentId(menu.getParentId()).name(menu.getMenuName()).build()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public static List<MenuTreeDto> convertMenuTree(List<SysMenu> menuList) {
        if (menuList != null && menuList.size() > 0) {
            return menuList.stream().map(menu -> MenuTreeDto.builder().id(menu.getId()).meunTitle(menu.getMenuName()).meunUrl(menu.getRouteUrl()).build()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
