package cn.x5456.summer.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    /**
     * 给定处理程序实例，请返回此 HandlerAdapter 是否可以支持它。
     */
    boolean supports(Object handler);

    /**
     * 使用给定的处理程序来处理此请求
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler);
}