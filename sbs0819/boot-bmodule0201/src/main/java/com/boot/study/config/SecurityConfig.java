package com.boot.study.config;


import com.boot.study.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 1.刚加入这个类，表示启用security，这时候，登录时，是走的security的登录页面，默认用户user，密码在控制台
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomNoAccessHandler customNoAccessHandler;

    @Autowired
    private CustomFilterSecurityInterceptor customFilterSecurityInterceptor;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 将自己重写的认证服务注册到认证管理器中
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * 这里进行拦截请求，将WebMvcConfig中的拦截代码注销掉
         *
         * antMatchers().permitAll() 是不进行拦截认证的路径资源，除了这些anyRequest().authenticated() 都是需要校验授权的的
         * successForwardUrl("/index") 登陆成功后的访问页面
         * loginProcessingUrl("/auth/login") 自定义登陆接口，默认是/login
         *
         *
         */
//        http.csrf()
//                .disable()
//                .authorizeRequests()
//                .antMatchers("/**/*.css", "/**/*.html", "/**/*.html", "/**/*.js", "/**/*.png",
//                        "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.svg", "/**/*.min.css", "/**/*.ttf", "/**/*.woff", "/**/*.woff2")
//                .permitAll()
//                .antMatchers("/loginPage", "/auth/**")// 这里对登录页放行
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/loginPage")
//                .loginProcessingUrl("/auth/login")
//                .successForwardUrl("/index")
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)
//        ;
        // 解决前端不同在iframe加载的报错
        // Refused to display in a frame because it set 'X-Frame-Options' to 'DENY'
        http.headers().frameOptions().sameOrigin();
        http.cors().and().csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(customNoAccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/**/*.css", "/**/*.html", "/**/*.html", "/**/*.js", "/**/*.png",
                        "/**/*.jpg", "/**/*.jpeg", "/**/*.png", "/**/*.svg", "/**/*.min.css", "/**/*.ttf", "/**/*.woff", "/**/*.woff2")
                .permitAll()
                .antMatchers("/loginPage", "/auth/**")// 这里对登录页放行
                .permitAll()
                .antMatchers("/swagger-ui.html")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/loginPage")
//                .loginProcessingUrl("/auth/mylogin")
                .successForwardUrl("/index")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
        ;
        // 解析jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class);


    }


    /**
     * 将认证管理器注册到spring容器中
     *
     * @return
     * @throws Exception
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**");
    }

    /**
     * 密码加解密的bean
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }


}
