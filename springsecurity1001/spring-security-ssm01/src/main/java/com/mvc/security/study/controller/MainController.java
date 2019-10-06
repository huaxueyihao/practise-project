package com.mvc.security.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    /**
     * 登陆页面
     * @return
     */
    @RequestMapping("/userLogin")
    public String login(){
        return "login";
    }

    /**
     * 错误页面
     * @return
     */
    @RequestMapping("/error")
    public String error(){
        return "error";
    }
}
