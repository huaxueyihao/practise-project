package com.boot.study.controller;

import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/getAllUser")
    @ResponseBody
    public List<SysUser> getAllUser(){
        return sysUserService.getAllUser();
    }


}
