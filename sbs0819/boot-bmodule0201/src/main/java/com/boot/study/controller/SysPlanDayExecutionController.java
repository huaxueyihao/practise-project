package com.boot.study.controller;

import com.boot.study.bean.DayExecutionDto;
import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysPlanDayExecution;
import com.boot.study.service.SysPlanDayExecutionService;
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
@RequestMapping("/planDayExecution")
public class SysPlanDayExecutionController extends BaseController {

    @Autowired
    private SysPlanDayExecutionService sysPlanDayExecutionService;


    @GetMapping("/getAll")
    @ResponseBody
    public JSONResponse getAll() {
        List<DayExecutionDto> dayExecutionDtoList = sysPlanDayExecutionService.getAll();
        return success(dayExecutionDtoList);
    }


    @PostMapping("/pageList")
    @ResponseBody
    public JSONResponse pageList(@RequestBody PageParam<SysPlanDayExecution> pageParam) {
        PageResult<SysPlanDayExecution> pageResult = sysPlanDayExecutionService.pageList(pageParam);
        return success(pageResult);
    }

    @PostMapping("/add")
    @ResponseBody
    public JSONResponse addUser(@RequestBody SysPlanDayExecution sysPlanDayExecution) {
        // 校验
        ValidatorUtil.validator(sysPlanDayExecution);
        sysPlanDayExecutionService.add(sysPlanDayExecution);
        return success(null);
    }

    @GetMapping("/remove/{id}")
    @ResponseBody
    public JSONResponse remove(@PathVariable Long id) {
        sysPlanDayExecutionService.remove(id);
        return success(null);
    }

    @PostMapping("/batchRemove")
    @ResponseBody
    public JSONResponse batchRemove(@RequestBody Long[] ids) {
        sysPlanDayExecutionService.batchRemove(ids);
        return success(null);
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    public JSONResponse detail(@PathVariable Long id) {
        SysPlanDayExecution sysPlanDayExecution = sysPlanDayExecutionService.detail(id);
        return success(sysPlanDayExecution);
    }

    @PostMapping("/update")
    @ResponseBody
    public JSONResponse updateUser(@RequestBody SysPlanDayExecution sysPlanDayExecution) {
        sysPlanDayExecutionService.update(sysPlanDayExecution);
        return success(null);
    }


}
