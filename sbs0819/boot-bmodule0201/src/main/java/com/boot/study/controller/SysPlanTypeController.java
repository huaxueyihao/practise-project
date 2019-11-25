package com.boot.study.controller;

import com.boot.study.bean.PlanTypeSelectDto;
import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysPlanType;
import com.boot.study.service.SysPlanTypeService;
import com.boot.study.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * controller
 */
@Slf4j
@Controller
@RequestMapping("/planType")
public class SysPlanTypeController extends BaseController {

    @Autowired
    private SysPlanTypeService sysPlanTypeService;


    @GetMapping("/getAll")
    @ResponseBody
    public JSONResponse getAll() {
        List<PlanTypeSelectDto> planTypeSelectDtoList = sysPlanTypeService.getAll();
        return success(planTypeSelectDtoList);
    }

    @PostMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestBody PageParam<SysPlanType> pageParam) {
        PageResult<SysPlanType> pageResult = sysPlanTypeService.pageList(pageParam);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse addUser(@RequestBody SysPlanType sysPlanType) {
        // 校验
        ValidatorUtil.validator(sysPlanType);
        sysPlanTypeService.add(sysPlanType);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id) {
        sysPlanTypeService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids) {
        sysPlanTypeService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id) {
        SysPlanType sysPlanType = sysPlanTypeService.detail(id);
        return success(sysPlanType);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse updateUser(@RequestBody SysPlanType sysPlanType) {
        sysPlanTypeService.update(sysPlanType);
        return success(null);
    }


}
