package cn.x5456.summer.web.method.support;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.request.ServletWebRequest;

public interface HandlerMethodReturnValueHandler {

    /**
     * 此处理程序是否支持给定的{@linkplain MethodParameter 方法返回类型}。
     */
    boolean supportsReturnType(MethodParameter returnType);

    /**
     * 通过向模型添加属性并设置视图
     * 或将{@link ModelAndViewContainer＃setRequestHandled}标志设置为{@code true}来处理给定的返回值，以指示已直接处理响应。
     */
    void handleReturnValue(Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer mavContainer, ServletWebRequest webRequest);

}
