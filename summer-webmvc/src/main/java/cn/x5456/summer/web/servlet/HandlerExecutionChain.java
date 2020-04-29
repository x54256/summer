package cn.x5456.summer.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {

    private final Object handler;

    private List<HandlerInterceptor> interceptorList = new ArrayList<>();

    public HandlerExecutionChain(Object handler) {
        this.handler = handler;
    }

    public Object getHandler() {
        return handler;
    }

    public void addInterceptor(HandlerInterceptor handlerInterceptor) {
        interceptorList.add(handlerInterceptor);
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) {
        for (HandlerInterceptor handlerInterceptor : interceptorList) {
            if (!handlerInterceptor.preHandle(request, response, this.handler)) {
                return false;
            }
        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
        for (HandlerInterceptor handlerInterceptor : interceptorList) {
            handlerInterceptor.postHandle(request, response, this.handler, mv);
        }
    }

    public void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        for (HandlerInterceptor handlerInterceptor : interceptorList) {
            handlerInterceptor.afterCompletion(request, response, handler, ex);
        }

    }
}