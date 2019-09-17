package com.boot.study.controller;

import com.boot.study.bean.MenuTreeDto;
import com.boot.study.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 首页controller
 */
@Controller
public class IndexController {


    @Resource
    private SysMenuService sysMenuService;

    /**
     * 首页渲染菜单
     * @param parentId 父菜单id
     * @return
     */
    @GetMapping({"/indexMenuTree", "/indexMenuTree/{parentId}"})
    @ResponseBody
    public List<MenuTreeDto> indexMenuTree(@PathVariable(required = false) Long parentId) {
        return sysMenuService.indexMenuTree(parentId);
    }
}
