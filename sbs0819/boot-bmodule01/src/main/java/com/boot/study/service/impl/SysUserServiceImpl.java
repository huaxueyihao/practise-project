package com.boot.study.service.impl;

import com.boot.study.common.PageResult;
import com.boot.study.mapper.SysUserMapper;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public List<SysUser> getAllUser() {
        return sysUserMapper.selectAll();
    }

    @Override
    public PageResult<SysUser> pageList(Integer page, Integer limit) {
        PageResult<SysUser> pageResult = new PageResult<>();
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();


        int count = sysUserMapper.selectCountByExample(example);
        if (count > 0) {
            int offset = (page - 1) * limit;
            RowBounds rowBounds = new RowBounds(offset, limit);
            List<SysUser> sysUsers = sysUserMapper.selectByExampleAndRowBounds(example, rowBounds);
            pageResult.setDataList(sysUsers);
            pageResult.setCount(count);
        }
        return pageResult;
    }

    @Override
    public void addUser(SysUser sysUser) {
        sysUserMapper.insert(sysUser);
    }

    @Override
    public SysUser detail(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(SysUser sysUser) {
        SysUser entity = sysUserMapper.selectByPrimaryKey(sysUser.getId());
        entity.setUserName(sysUser.getUserName());
        entity.setAge(sysUser.getAge());
        entity.setPassword(sysUser.getPassword());
        entity.setSex(sysUser.getSex());
        sysUserMapper.updateByPrimaryKey(entity);
    }
}
