package com.boot.study.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomNoAccessHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String msg = "{\"success\" : false,\"status\":" + HttpServletResponse.SC_NOT_ACCEPTABLE + ", \"message\" : \"" + accessDeniedException.getMessage() + "\"}";
        response.setContentType("json");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(msg.getBytes());
        outputStream.flush();
    }


}
