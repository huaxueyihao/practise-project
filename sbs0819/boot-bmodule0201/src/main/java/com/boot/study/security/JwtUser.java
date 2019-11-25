package com.boot.study.security;

import com.boot.study.model.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 实现了spring-security定义的用户封装对象
 * JwtUser
 *
 */
public class JwtUser implements UserDetails {

    private Long id;

    private String username;

    private String password;

    // 权限集合
    private Collection<? extends  GrantedAuthority> authorities;

    public JwtUser(SysUser user){
        this.id = user.getId();
        this.username = user.getUserName();
        this.password = user.getPassword();
//        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_AMDIN"));
    }

    public JwtUser(SysUser user, List<GrantedAuthority> grantedAuthorities){
        this.id = user.getId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.authorities = grantedAuthorities;
    }

    /**
     * 获取权限信息
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账号是否过期，默认false
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否锁定，默认false
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证是否未过期，默认是false，
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
