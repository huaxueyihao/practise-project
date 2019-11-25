package com.boot.study.service.impl;

import com.boot.study.bean.PlanTypeSelectDto;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.exception.BusinessException;
import com.boot.study.mapper.SysPlanTypeMapper;
import com.boot.study.model.SysPlanType;
import com.boot.study.service.SysPlanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SysPlanTypeServiceImpl implements SysPlanTypeService {


    @Autowired
    private SysPlanTypeMapper sysPlanTypeMapper;


    @Override
    public void add(SysPlanType sysPlanType) {
        if (sysPlanTypeMapper.countByTypeName(sysPlanType.getTypeName()) > 0) {
            throw new BusinessException(100, "该类型已经存在");
        }
        sysPlanType.setUserId(1L);
        sysPlanTypeMapper.insert(sysPlanType);
    }

    @Override
    public SysPlanType detail(Long id) {
        return sysPlanTypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(SysPlanType sysPlanType) {
        SysPlanType entity = sysPlanTypeMapper.selectByPrimaryKey(sysPlanType.getId());

        if (entity != null) {
            entity.setRemark(sysPlanType.getRemark());
            entity.setIcon(sysPlanType.getIcon());
            entity.setBgColor(sysPlanType.getBgColor());
            entity.setTypeDesc(sysPlanType.getTypeDesc());
            sysPlanTypeMapper.updateByPrimaryKey(entity);
        }

    }

    @Override
    public void remove(Long id) {
        sysPlanTypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchRemove(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            sysPlanTypeMapper.deleteByPrimaryKey(id);
        });
    }

    @Override
    public PageResult<SysPlanType> pageList(PageParam<SysPlanType> pageParam) {
        int limit = pageParam.getLimit();
        int page = pageParam.getPage();
        SysPlanType condition = pageParam.getCondition();
        PageResult<SysPlanType> pageResult = new PageResult<>();
        Example example = new Example(SysPlanType.class);
        Example.Criteria criteria = example.createCriteria();
        Optional.ofNullable(condition).ifPresent(param -> {
            criteria.andLike("typeDesc", "%" + param.getTypeDesc() + "%");
        });
        int count = sysPlanTypeMapper.selectCountByExample(example);
        if (count > 0) {
            List<SysPlanType> sysUsers = sysPlanTypeMapper.selectByExampleAndRowBounds(example, pageParam.getRowBounds());
            pageResult.setDataList(sysUsers);
            pageResult.setCount(count);
        }
        return pageResult;
    }

    @Override
    public List<PlanTypeSelectDto> getAll() {
        List<SysPlanType> sysPlanTypes = sysPlanTypeMapper.selectAll();
        return sysPlanTypes.stream().map(plan ->
                PlanTypeSelectDto.builder().name(plan.getTypeDesc()).
                        value(plan.getTypeName() + "-" + plan.getTypeDesc()).build())
                .collect(Collectors.toList());
    }
}
