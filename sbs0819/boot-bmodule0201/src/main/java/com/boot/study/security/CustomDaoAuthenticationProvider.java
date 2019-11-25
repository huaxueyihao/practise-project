package com.boot.study.security;

import com.boot.study.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

public class CustomDaoAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService myUserDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String presentedPassword = authentication.getCredentials().toString();
        String username = authentication.getPrincipal().toString();
        System.out.println("username=" + username);
        UserDetails userDetails = null;//myUserDetailsService.loadUserById(authentication.getPrincipal().toString());

        if (authentication.getCredentials() == null) {
            throw new BusinessException("未认证");
        } else {
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                throw new BusinessException("密码不批配");
            }
        }
        return new UsernamePasswordAuthenticationToken(userDetails, presentedPassword, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
