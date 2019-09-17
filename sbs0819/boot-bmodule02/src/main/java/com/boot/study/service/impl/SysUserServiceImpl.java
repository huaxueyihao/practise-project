package com.boot.study.service.impl;

import com.boot.study.exception.BusinessException;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import com.boot.study.common.PageParam;
import com.boot.study.common.PageResult;
import com.boot.study.mapper.SysUserMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(100, "用户不存在");
        }
        return sysUser;
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

    @Override
    public void remove(Long id) {
        sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void batchRemove(Long[] ids) {
        Optional.of(ids).ifPresent(idList -> {
            for (Long id : idList) {
                sysUserMapper.deleteByPrimaryKey(id);
            }
        });
    }

    @Override
    public PageResult<SysUser> pageList(PageParam<SysUser> pageParam) {
        int limit = pageParam.getLimit();
        int page = pageParam.getPage();
        SysUser condition = pageParam.getCondition();
        PageResult<SysUser> pageResult = new PageResult<>();
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        Optional.ofNullable(condition).ifPresent(userParam -> {
            criteria.andLike("userName", "%" + userParam.getUserName() + "%");
        });
        int count = sysUserMapper.selectCountByExample(example);
        if (count > 0) {
            List<SysUser> sysUsers = sysUserMapper.selectByExampleAndRowBounds(example, pageParam.getRowBounds());
            pageResult.setDataList(sysUsers);
            pageResult.setCount(count);
        }
        return pageResult;
    }

    @Override
    public boolean checkByUserNameAndPassword(String userName, String password) {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        Optional.ofNullable(userName).ifPresent(name -> criteria.andEqualTo("userName", name));
        Optional.ofNullable(password).ifPresent(pass -> criteria.andEqualTo("password", pass));
        return sysUserMapper.selectCountByExample(example) > 0 ? true : false;
    }
}
