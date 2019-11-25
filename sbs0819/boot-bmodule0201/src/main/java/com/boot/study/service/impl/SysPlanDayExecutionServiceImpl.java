package com.boot.study.service.impl;

import com.boot.study.bean.DayExecutionDto;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.mapper.SysPlanDayExecutionMapper;
import com.boot.study.mapper.SysPlanTypeMapper;
import com.boot.study.model.SysPlanDayExecution;
import com.boot.study.model.SysPlanType;
import com.boot.study.service.SysPlanDayExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysPlanDayExecutionServiceImpl implements SysPlanDayExecutionService {

    @Autowired
    private SysPlanDayExecutionMapper sysPlanDayExecutionMapper;

    @Autowired
    private SysPlanTypeMapper sysPlanTypeMapper;

    @Override
    public void add(SysPlanDayExecution sysPlanDayExecution) {
        String typeName = sysPlanDayExecution.getTypeName();
        Optional.ofNullable(typeName).ifPresent(name -> {
            String[] split = name.split("-");
            sysPlanDayExecution.setTypeName(split[0]);
            sysPlanDayExecution.setTypeDesc(split[1]);
        });
        sysPlanDayExecution.setUserId(1L);
        // 计算耗时
        LocalDateTime endTime = sysPlanDayExecution.getEndTime();
        LocalDateTime startTime = sysPlanDayExecution.getStartTime();
        long useTime = Duration.between(endTime, startTime).toMinutes();
        sysPlanDayExecution.setUseTime((int) useTime);

        sysPlanDayExecutionMapper.insert(sysPlanDayExecution);
    }

    @Override
    public SysPlanDayExecution detail(Long id) {
        return sysPlanDayExecutionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(SysPlanDayExecution sysPlanDayExecution) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void batchRemove(Long[] ids) {

    }

    @Override
    public PageResult<SysPlanDayExecution> pageList(PageParam<SysPlanDayExecution> pageParam) {
        return null;
    }

    @Override
    public List<DayExecutionDto> getAll() {
        List<SysPlanDayExecution> sysPlanDayExecutions = sysPlanDayExecutionMapper.selectAll();
        return sysPlanDayExecutions.stream().map(plan -> {
            Example example = new Example(SysPlanType.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("typeName", plan.getTypeName());
            SysPlanType planType = sysPlanTypeMapper.selectOneByExample(example);
            return DayExecutionDto.builder().
                    start(plan.getStartTime()).end(plan.getEndTime()).backgroundColor(planType.getBgColor()).
                    groupId(1L).title(plan.getTitle()).textColor("#fff").build();
        }).collect(Collectors.toList());
    }
}
