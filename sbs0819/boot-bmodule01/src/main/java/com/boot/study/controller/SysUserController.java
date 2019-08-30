package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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


}
