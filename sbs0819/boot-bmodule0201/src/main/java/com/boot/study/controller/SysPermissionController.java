package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysPermission;
import com.boot.study.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户controller
 */
@Controller
@RequestMapping("/permission")
public class SysPermissionController extends BaseController {

    @Autowired
    private SysPermissionService sysPermissionService;



    @PostMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestBody PageParam<SysPermission> pageParam) {
        PageResult<SysPermission> pageResult = sysPermissionService.pageList(pageParam);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse add(@RequestBody SysPermission sysPermission) {
        sysPermissionService.add(sysPermission);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id) {
        sysPermissionService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids) {
        sysPermissionService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id) {
        SysPermission sysPermission = sysPermissionService.detail(id);
        return success(sysPermission);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse update(@RequestBody SysPermission sysPermission) {
        sysPermissionService.update(sysPermission);
        return success(null);
    }


}
