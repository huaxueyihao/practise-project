package com.boot.study.security;

import com.boot.study.common.JSONResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        LOGGER.error("Responding with unauthorize error Message - {}", e.getMessage());
        JSONResponse result = JSONResponse.builder().success(false).msg(e.getMessage()).code(Long.valueOf(HttpServletResponse.SC_UNAUTHORIZED)).build();
        response.setContentType("application/json,charset=utf-8");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(objectMapper.writeValueAsBytes(result));
        outputStream.flush();
    }
}
