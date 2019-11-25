package com.boot.study.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UsernamePasswordAuthenticationFilter 是security自己提供的过滤器，重写其中的成功方法(successfulAuthentication),登陆失败重写(unsuccessfulAuthentication)
 * 1.成功回调中用到一个TokenAuthenticationHandler,即token认证处理类，该类的主要方法就是借用jwt的机制生成token，供后面登陆授权使用。
 * 2.忘response头信息中翻入参数为"Authorization",值为Bearer + token的值
 *
 *
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;

    public JWTLoginFilter() {
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        Object obj = authResult.getPrincipal();

        if(obj != null){
            UserDetails userDetails = (UserDetails) obj;


        }

    }
}
