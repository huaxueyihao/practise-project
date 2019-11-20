package com.boot.study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * spring的资源访问控制的配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public final static String SESSION_KEY = "user";

    private final long MAX_AGE_SECS = 3600;

    /**
     * controller请求拦截器
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/","/**")
//                .excludePathPatterns("/loginPage","/login")
//                .excludePathPatterns("/**/*.html","/**/*.css", "/**/*.js",
//                        "/**/*.png", "/**/*.jpg", "/**/*.jpeg","/**/*.svg",
//                        "/**/*.min.css","/**/*.ttf","/**/*.woff");
//    }

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
        /**
         * "/" 这个一定要配置，不配置，不会被拦截，就是直接访问localhost:8080默认跳转到index页面
         * 但是，却跳过了登陆，直接访问了index.html页面，配置后就会被拦截跳转到登陆页面
         */
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/loginPage").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/user/index").setViewName("user/index");
        registry.addViewController("/user/addUser").setViewName("user/addUser");
        registry.addViewController("/menu/index").setViewName("menu/index");
        registry.addViewController("/menu/menuPage").setViewName("menu/menuPage");
        registry.addViewController("/file/index").setViewName("file/index");
        registry.addViewController("/file/filePage").setViewName("file/filePage");
        registry.addViewController("/plan/index").setViewName("plan/index");

        registry.addViewController("/plan/dayIndex").setViewName("plan/dayIndex");
        registry.addViewController("/plan/typeIndex").setViewName("plan/typeIndex");
        registry.addViewController("/plan/typePage").setViewName("plan/typePage");
        registry.addViewController("/plan/dayExecutionIndex").setViewName("plan/dayExecutionIndex");
        registry.addViewController("/plan/dayExecutionIndex2").setViewName("plan/dayExecutionIndex2");
        registry.addViewController("/plan/dayExecutionPage").setViewName("plan/dayExecutionPage");
        registry.addViewController("/plan/dayExecutionPage2").setViewName("plan/dayExecutionPage2");


    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
}
