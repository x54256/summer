package cn.x5456.summer.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {

    /**
     * Controller执行前调用此方法
     */
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * Controller执行后但未返回视图前调用此方法
     */
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView);

    /**
     * Controller执行后且视图返回后调用此方法
     */
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);

}