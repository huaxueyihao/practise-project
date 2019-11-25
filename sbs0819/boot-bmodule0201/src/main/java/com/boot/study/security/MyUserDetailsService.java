package com.boot.study.security;

import com.boot.study.exception.BusinessException;
import com.boot.study.mapper.SysPermissionMapper;
import com.boot.study.mapper.SysUserMapper;
import com.boot.study.model.SysPermission;
import com.boot.study.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 实现security 的登录认证接口
 */
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        if (sysUser == null) {
            throw new BusinessException(100, "该用户不存在");
        }
//        System.out.println(passwordEncoder.encode(sysUser.getPassword()));
//        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        return new JwtUser(sysUser);
    }

    public UserDetails loadUserById(Long userId) {

        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<SysPermission> permissions = sysPermissionMapper.selectPermissionListByUserId(userId);
        if (permissions != null && permissions.size() > 0) {
            permissions.forEach(permission -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermName()));
            });
        }
        return new JwtUser(user, grantedAuthorities);
    }
}
