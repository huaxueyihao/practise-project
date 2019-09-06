package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;
import com.boot.study.common.TreeDto;
import com.boot.study.common.ZTreeDto;
import com.boot.study.model.SysMenu;
import com.boot.study.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
public class SysMenuController extends BaseController {

    @Autowired
    private SysMenuService sysMenuService;


    @RequestMapping({"/index"})
    public String index(Model model) {
        return "menu/index";
    }

    @RequestMapping({"/menuPage"})
    public String addUserPage(Model model) {
        return "menu/menuPage";
    }

    @GetMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestParam("page") Integer page,
                                 @RequestParam("limit") Integer limit,
                                 @RequestParam(value = "menuName", required = false) String menuName
    ) {
        PageResult<SysMenu> pageResult = sysMenuService.pageList(page, limit, menuName);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse addUser(@RequestBody SysMenu sysMenu) {
        sysMenuService.addUser(sysMenu);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id) {
        sysMenuService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids) {
        sysMenuService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id) {
        SysMenu sysMenu = sysMenuService.detail(id);
        return success(sysMenu);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse updateUser(@RequestBody SysMenu sysMenu) {
        sysMenuService.update(sysMenu);
        return success(null);
    }


    @GetMapping({"/menuTree", "/menuTree/{parentId}"})
    @ResponseBody
    public JSONResponse menuTree(@PathVariable(required = false) Long parentId) {
        List<TreeDto> treeDtos = sysMenuService.menuTree(parentId);
        return success(treeDtos);
    }

    @GetMapping({"/menuZTree", "/menuZTree/{parentId}"})
    @ResponseBody
    public List<ZTreeDto> menuZTree(@PathVariable(required = false) Long parentId) {
        List<ZTreeDto> treeDtos = sysMenuService.menuZTree(parentId);
        return treeDtos;
    }



}
