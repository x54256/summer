package cn.x5456.summer.web.servlet;

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
}