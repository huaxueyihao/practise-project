package com.boot.study.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HandlerInterceptor是SpringWebMVC的拦截器，类似于Servlet开发中的过滤器Filter，用于对请求进行拦截和处理
 * <p>
 * 1、权限检查：如检测请求是否具有登录权限，如果没有直接返回到登陆页面。
 * 2、性能监控：用请求处理前和请求处理后的时间差计算整个请求响应完成所消耗的时间。
 * 3、日志记录：可以记录请求信息的日志，以便进行信息监控、信息统计等。
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * preHandle：在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("进入拦截器了url=" + requestURI);
        Object user = request.getSession().getAttribute("user");
        if (user == null || user.equals("")) {
            response.sendRedirect("/loginPage");
            return false;
        }
        return true;
    }

    /**
     * postHandle：请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * afterCompletion：在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
