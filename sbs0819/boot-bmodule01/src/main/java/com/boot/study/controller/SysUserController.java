package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping({"/index"})
    public String index(Model model) {
        return "user/index";
    }

    @RequestMapping({"/addUser"})
    public String addUserPage(Model model) {
        return "user/addUser";
    }

    @GetMapping("/getAllUser")
    @ResponseBody
    public List<SysUser> getAllUser() {
        return sysUserService.getAllUser();
    }

    @GetMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        PageResult<SysUser> pageResult = sysUserService.pageList(page, limit);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse addUser(@RequestBody SysUser sysUser){
        sysUserService.addUser(sysUser);
        return success(null);
    }

    @PostMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse updateUser(@PathVariable Long id){
        SysUser sysUser = sysUserService.detail(id);
        return success(sysUser);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse updateUser(@RequestBody SysUser sysUser){
        sysUserService.update(sysUser);
        return success(null);
    }


}
