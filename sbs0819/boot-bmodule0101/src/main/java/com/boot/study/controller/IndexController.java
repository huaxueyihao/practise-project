package com.boot.study.controller;

import com.boot.study.bean.MenuTreeDto;
import com.boot.study.service.SysMenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class IndexController {


    @Resource
    private SysMenuService sysMenuService;

//    @RequestMapping({"/index"})
//    public String index(Model model) {
//        return "index";
//    }

//    @RequestMapping({"/dashboard"})
//    public String main() {
//        return "dashboard";
//    }


    @GetMapping({"/indexMenuTree", "/indexMenuTree/{parentId}"})
    @ResponseBody
    public List<MenuTreeDto> indexMenuTree(@PathVariable(required = false) Long parentId) {
        return sysMenuService.indexMenuTree(parentId);
    }
}
