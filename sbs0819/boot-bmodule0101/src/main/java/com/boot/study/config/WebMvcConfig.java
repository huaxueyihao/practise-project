package com.boot.study.config;

import com.boot.study.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public final static String SESSION_KEY = "user";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("*","/","/**")
                .excludePathPatterns("/loginPage","/login")
                .excludePathPatterns("/**/*.html","/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/loginPage").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/user/index").setViewName("user/index");
        registry.addViewController("/user/addPage").setViewName("user/addPage");
        registry.addViewController("/menu/index").setViewName("menu/index");
        registry.addViewController("/menu/menuPage").setViewName("menu/menuPage");
    }
}
