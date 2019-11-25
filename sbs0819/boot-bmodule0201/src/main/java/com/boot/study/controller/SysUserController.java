package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import com.boot.study.util.ValidatorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户controller
 */
@Controller
@RequestMapping("/user")
@Api(value = "用户的增删改查")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/getAllUser")
    @ResponseBody
    @ApiOperation(value = "获取所有用户", notes = "无参")
    public List<SysUser> getAllUser() {
        return sysUserService.getAllUser();
    }

    @GetMapping("/getPageList")
    @ResponseBody
    public JSONResponse pageList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        PageResult<SysUser> pageResult = sysUserService.pageList(page, limit);
        return success(pageResult);
    }

    @PostMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestBody PageParam<SysUser> pageParam) {
        PageResult<SysUser> pageResult = sysUserService.pageList(pageParam);
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


    @PutMapping("/addRole/{userId}")
    @ResponseBody
    public JSONResponse addRole(@PathVariable Long userId, @RequestBody List<Long> roleIdList) {
        int count = sysUserService.addRole(userId, roleIdList);
        return success(count);
    }


    @DeleteMapping("/removeRole/{userId}")
    @ResponseBody
    public JSONResponse removeRole(@PathVariable Long userId, @RequestBody List<Long> roleIdList) {
        int count = sysUserService.removeRole(userId, roleIdList);
        return success(count);
    }

}
