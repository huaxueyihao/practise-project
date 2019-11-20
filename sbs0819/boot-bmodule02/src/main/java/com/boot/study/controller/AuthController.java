package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.model.SysUser;
import com.boot.study.security.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    @ResponseBody
    public JSONResponse login(@RequestBody SysUser user) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        Object principal = authenticate.getPrincipal();
        System.out.println(principal.getClass());
        JwtUser jwtUser = null;
        if (principal instanceof JwtUser) {
            jwtUser = (JwtUser) principal;
        }
        return this.success(jwtUser);
    }


    @GetMapping("/logout")
    @ResponseBody
    public JSONResponse logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(null);
        return this.success(null);
    }

}
