package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysRole;
import com.boot.study.service.SysRoleService;
import com.boot.study.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户controller
 */
@Controller
@RequestMapping("/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;



    @PostMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestBody PageParam<SysRole> pageParam) {
        PageResult<SysRole> pageResult = sysRoleService.pageList(pageParam);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse add(@RequestBody SysRole sysRole) {
        sysRoleService.add(sysRole);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id) {
        sysRoleService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids) {
        sysRoleService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.detail(id);
        return success(sysRole);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse update(@RequestBody SysRole sysRole) {
        sysRoleService.update(sysRole);
        return success(null);
    }


    @PutMapping("/addPermission/{roleId}")
    @ResponseBody
    public JSONResponse addPermission(@PathVariable Long roleId, @RequestBody List<Long> permIdList) {
        int count = sysRoleService.addPermission(roleId, permIdList);
        return success(count);
    }


    @DeleteMapping("/removePermission/{roleId}")
    @ResponseBody
    public JSONResponse removePermission(@PathVariable Long roleId, @RequestBody List<Long> permIdList) {
        int count = sysRoleService.removePermission(roleId, permIdList);
        return success(count);
    }


}
