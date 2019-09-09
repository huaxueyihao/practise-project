package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.config.WebMvcConfig;
import com.boot.study.exeception.BusinessException;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
public class LoginController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

//    @RequestMapping({"/", "/loginPage"})
//    public String index(Model model) {
//        return "login";
//    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse login(HttpServletRequest request, @RequestBody(required = false) SysUser sysUser) {
        String userName = sysUser.getUserName();
        String password = sysUser.getPassword();
        HttpSession session = request.getSession();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new BusinessException(100, "用户名、密码不能为空");
        }
        if (sysUserService.checkByUserNameAndPassword(userName, password)) {
            session.setAttribute("user", WebMvcConfig.SESSION_KEY);
            Map<String, String> map = new HashMap<>();
            map.put("userName", userName);
            return success(map);
        } else {
            // 是用户名不对
            if (!sysUserService.checkByUserNameAndPassword(userName, null)) {
                throw new BusinessException(100, "该用户名不存在");
            }
            // 密码不对
            throw new BusinessException(100, "密码不对");
        }

    }


    @GetMapping(value = "/logout")
    @ResponseBody
    public JSONResponse logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return success(null);
    }

}
