package com.boot.study.service.impl;

import com.boot.study.mapper.SysUserMapper;
import com.boot.study.model.SysUser;
import com.boot.study.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public List<SysUser> getAllUser() {
        return sysUserMapper.selectAll();
    }
}
