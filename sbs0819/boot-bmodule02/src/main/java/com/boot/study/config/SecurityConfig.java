package com.boot.study.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 1.刚加入这个类，表示启用security，这时候，登录时，是走的security的登录页面，默认用户user，密码在控制台
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * 这里进行拦截请求，将WebMvcConfig中的拦截代码注销掉
         *
         * antMatchers().permitAll() 是不进行拦截认证的路径资源，除了这些anyRequest().authenticated() 都是需要校验授权的的
         * successForwardUrl("/index") 登陆成功后的访问页面
         *
         *
         */
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/**/*.css", "/**/*.html", "/**/*.html", "/**/*.js", "/**/*.png",
                        "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.svg", "/**/*.min.css", "/**/*.ttf", "/**/*.woff", "/**/*.woff2")
                .permitAll()
                .antMatchers( "/loginPage")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginPage("/loginPage").successForwardUrl("/index")
        ;

    }


    /**
     * 密码加解密的bean
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
