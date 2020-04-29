package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.method.support.HandlerMethodReturnValueHandler;
import cn.x5456.summer.web.method.support.ModelAndViewContainer;
import cn.x5456.summer.web.request.ServletWebRequest;

public class ViewNameMethodReturnValueHandler implements HandlerMethodReturnValueHandler {

    /**
     * 此处理程序是否支持给定的{@linkplain MethodParameter 方法返回类型}。
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Class<?> paramType = returnType.getParameterType();
        return (Void.TYPE.equals(paramType) || CharSequence.class.isAssignableFrom(paramType));
    }

    /**
     * 通过向模型添加属性并设置视图
     * 或将{@link ModelAndViewContainer ＃setRequestHandled}标志设置为{@code true}来处理给定的返回值，以指示已直接处理响应。
     */
    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, ServletWebRequest webRequest) {
        if (returnValue instanceof String) {
            // 将视图名设置进 mavContainer
            String viewName = returnValue.toString();
            mavContainer.setView(viewName);
        }
    }
}