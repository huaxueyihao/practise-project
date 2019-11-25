package com.boot.study.service.impl;

import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.exception.BusinessException;
import com.boot.study.mapper.SysPermissionMapper;
import com.boot.study.model.SysPermission;
import com.boot.study.model.SysRole;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;

@Service
public class SysPermissionServiceImpl implements SysPermissionService{

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public void add(SysPermission sysPermission) {
        if (sysPermissionMapper.selectCountByPermName(sysPermission.getPermName()) > 0) {
            throw new BusinessException(sysPermission.getPermName() + "权限已经存在");
        }
        sysPermissionMapper.insert(sysPermission);
    }

    @Override
    public SysPermission detail(Long id) {
        return sysPermissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(SysPermission sysPermission) {

    }

    @Override
    public void remove(Long id) {

    }

    @Override
    public void batchRemove(Long[] ids) {

    }

    @Override
    public PageResult<SysPermission> pageList(PageParam<SysPermission> pageParam) {
        SysPermission condition = pageParam.getCondition();
        PageResult<SysPermission> pageResult = new PageResult<>();
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        Optional.ofNullable(condition).ifPresent(permissionParam -> {
            criteria.andLike("permDesc", "%" + permissionParam.getPermDesc() + "%");
        });
        int count = sysPermissionMapper.selectCountByExample(example);
        if (count > 0) {
            List<SysPermission> list = sysPermissionMapper.selectByExampleAndRowBounds(example, pageParam.getRowBounds());
            pageResult.setDataList(list);
            pageResult.setCount(count);
        }
        return pageResult;
    }
}
