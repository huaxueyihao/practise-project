package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysMenu;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysMenuService;
import com.boot.study.service.SysUserService;
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

    @RequestMapping({"/addMenu"})
    public String addUserPage(Model model) {
        return "user/menuPage";
    }

    @GetMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestParam("page") Integer page,
                                 @RequestParam("limit") Integer limit,
                                 @RequestParam(value = "menuName",required = false) String menuName
                                 ) {
        PageResult<SysMenu> pageResult = sysMenuService.pageList(page, limit,menuName);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse addUser(@RequestBody SysMenu sysMenu){
        sysMenuService.addUser(sysMenu);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id){
        sysMenuService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids){
        sysMenuService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id){
        SysUser sysUser = sysMenuService.detail(id);
        return success(sysUser);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse updateUser(@RequestBody SysMenu sysMenu){
        sysMenuService.update(sysMenu);
        return success(null);
    }


}
