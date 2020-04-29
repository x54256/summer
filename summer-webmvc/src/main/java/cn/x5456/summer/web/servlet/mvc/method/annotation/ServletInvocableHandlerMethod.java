package cn.x5456.summer.web.servlet.mvc.method.annotation;

import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.method.HandlerMethod;
import cn.x5456.summer.web.method.support.HandlerMethodReturnValueHandlerComposite;
import cn.x5456.summer.web.method.support.InvocableHandlerMethod;
import cn.x5456.summer.web.method.support.ModelAndViewContainer;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/04/27 16:19
 */
public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {

    // 返回值解析器
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public ServletInvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }

    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer) {

        // 执行方法
        Object returnValue = super.invokeForRequest(webRequest, mavContainer);

        // 将请求结束设置为 false，虽然默认就是 false
        mavContainer.setRequestHandled(false);

        // 通过返回值解析器，对结果进行处理
        returnValueHandlers.handleReturnValue(returnValue, this.getReturnValueType(), mavContainer, webRequest);
    }

    private MethodParameter getReturnValueType() {
        return new MethodParameter(super.getMethod(), -1);
    }
}
