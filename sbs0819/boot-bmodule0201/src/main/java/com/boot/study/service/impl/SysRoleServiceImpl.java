package com.boot.study.service.impl;


import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.exception.BusinessException;
import com.boot.study.mapper.SysPermissionMapper;
import com.boot.study.mapper.SysRoleMapper;
import com.boot.study.model.SysPermission;
import com.boot.study.model.SysRole;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.security.Permission;
import java.util.List;
import java.util.Optional;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public void add(SysRole sysRole) {
        if (sysRoleMapper.selectCountByRoleName(sysRole.getRoleName()) > 0) {
            throw new BusinessException(sysRole.getRoleName() + "角色已经存在");
        }
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public SysRole detail(Long id) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);
        List<SysPermission> list = sysPermissionMapper.selectPermissionListByRoleId(id);
        sysRole.setPermissionList(list);
        return sysRole;
    }

    @Override
    public void update(SysRole sysRole) {

    }

    @Override
    public void remove(Long id) {
        sysRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchRemove(Long[] ids) {

    }

    @Override
    public PageResult<SysRole> pageList(PageParam<SysRole> pageParam) {
        SysRole condition = pageParam.getCondition();
        PageResult<SysRole> pageResult = new PageResult<>();
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        Optional.ofNullable(condition).ifPresent(roleParam -> {
            criteria.andLike("roleDesc", "%" + roleParam.getRoleDesc() + "%");
        });
        int count = sysRoleMapper.selectCountByExample(example);
        if (count > 0) {
            List<SysRole> list = sysRoleMapper.selectByExampleAndRowBounds(example, pageParam.getRowBounds());
            pageResult.setDataList(list);
            pageResult.setCount(count);
        }
        return pageResult;
    }

    @Override
    public int addPermission(Long roleId, List<Long> permIdList) {
        int addCount = 0;
        if (permIdList != null && permIdList.size() > 0) {
            for (Long permId : permIdList) {
                int existCount = sysRoleMapper.selectCountByRoleIdAndPermId(roleId, permId);
                if (existCount <= 0) {
                    int count = sysRoleMapper.insertRolePerm(roleId, permId);
                    addCount += count;
                }
            }
        }
        return addCount;
    }

    @Override
    public int removePermission(Long roleId, List<Long> permIdList) {
        int removeCount = 0;
        if (permIdList != null && permIdList.size() > 0) {
            for (Long permId : permIdList) {
                removeCount = +sysRoleMapper.deleteRolePerm(roleId, permId);
            }
        }
        return removeCount;
    }
}
