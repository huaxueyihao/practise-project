package com.boot.study.security;

import com.boot.study.exception.BusinessException;
import com.boot.study.mapper.SysUserMapper;
import com.boot.study.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * 实现security 的登录认证接口
 *
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        if(sysUser == null){
            throw new BusinessException(100,"该用户不存在");
        }

        return new JwtUser(sysUser);
    }
}
