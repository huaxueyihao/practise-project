package com.boot.study.config;

import com.boot.study.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * spring的资源访问控制的配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public final static String SESSION_KEY = "user";

    /**
     * controller请求拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/","/**")
                .excludePathPatterns("/loginPage","/login")
                .excludePathPatterns("/**/*.html","/**/*.css", "/**/*.js",
                        "/**/*.png", "/**/*.jpg", "/**/*.jpeg","/**/*.svg",
                        "/**/*.min.css","/**/*.ttf","/**/*.woff");
    }

    /**
     * 静态资源位置映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }


    /**
     * 页面请求和页面的映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/loginPage").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/user/index").setViewName("user/index");
        registry.addViewController("/user/addUser").setViewName("user/addUser");
        registry.addViewController("/menu/index").setViewName("menu/index");
        registry.addViewController("/menu/menuPage").setViewName("menu/menuPage");
        registry.addViewController("/file/index").setViewName("file/index");
        registry.addViewController("/file/filePage").setViewName("file/filePage");
    }
}
