package com.boot.study.service;

import com.boot.study.bean.DayExecutionDto;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.model.SysPlanDayExecution;

import java.util.List;

public interface SysPlanDayExecutionService {


    /**
     * 增加
     * @param sysPlanDayExecution
     */
    void add(SysPlanDayExecution sysPlanDayExecution);

    /**
     * 详情
     * @param id
     * @return
     */
    SysPlanDayExecution detail(Long id);

    /**
     * 修改
     * @param sysPlanDayExecution
     */
    void update(SysPlanDayExecution sysPlanDayExecution);

    /**
     * 删除
     * @param id
     */
    void remove(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void batchRemove(Long[] ids);

    /**
     * 分页查询
     * @param pageParam
     * @return
     */
    PageResult<SysPlanDayExecution> pageList(PageParam<SysPlanDayExecution> pageParam);

    /**
     * 获得全部的数据
     * @return
     */
    List<DayExecutionDto> getAll();
}
