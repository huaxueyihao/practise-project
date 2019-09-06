package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;
import com.boot.study.exeception.BusinessException;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import com.boot.study.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.List;
import java.util.Set;

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
    public JSONResponse addUser(@RequestBody SysUser sysUser) {
        // 校验
        ValidatorUtil.validator(sysUser);
        sysUserService.addUser(sysUser);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id) {
        sysUserService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids) {
        sysUserService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id) {
        SysUser sysUser = sysUserService.detail(id);
        return success(sysUser);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse updateUser(@RequestBody SysUser sysUser) {
        sysUserService.update(sysUser);
        return success(null);
    }


}
