package cn.x5456.summer.web.method.support;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.util.ArrayList;
import java.util.List;

public class HandlerMethodReturnValueHandlerComposite implements HandlerMethodReturnValueHandler {

    private final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

    /**
     * 此处理程序是否支持给定的{@linkplain MethodParameter 方法返回类型}。
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return this.getReturnValueHandler(returnType) != null;
    }

    private HandlerMethodReturnValueHandler getReturnValueHandler(MethodParameter returnType) {
        for (HandlerMethodReturnValueHandler handler : this.returnValueHandlers) {
            if (handler.supportsReturnType(returnType)) {
                return handler;
            }
        }
        return null;
    }

    /**
     * 通过向模型添加属性并设置视图
     * 或将{@link ModelAndViewContainer＃setRequestHandled}标志设置为{@code true}来处理给定的返回值，以指示已直接处理响应。
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, ServletWebRequest webRequest) {
        HandlerMethodReturnValueHandler handler = this.getReturnValueHandler(returnType);
        if (handler == null) {
            throw new IllegalArgumentException("Unknown parameter type [" + returnType.getParameterType().getName() + "]");
        }

        handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    public HandlerMethodReturnValueHandlerComposite addHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        returnValueHandlers.addAll(handlers);
        return this;
    }
}