package cn.x5456.summer.web.method.support;

import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.core.MethodParameter;
import cn.x5456.summer.web.method.HandlerMethod;
import cn.x5456.summer.web.request.ServletWebRequest;

import java.lang.reflect.Method;

/**
 * 可以执行的处理器方法对象
 */
public class InvocableHandlerMethod extends HandlerMethod {

    private HandlerMethodArgumentResolverComposite argumentResolvers;

    private WebDataBinderFactory dataBinderFactory;

    public InvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public InvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    /**
     * 执行方法
     */
    public Object invokeForRequest(ServletWebRequest webRequest, ModelAndViewContainer mavContainer) {

        // 获取到当前方法的参数
        MethodParameter[] parameters = this.getParameters();

        // 使用参数解析器，将其从请求中取出
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i] = this.argumentResolvers.resolveArgument(parameters[i], mavContainer, webRequest, this.dataBinderFactory);
        }

        // 执行方法
        return ReflectUtil.invoke(getBean(), getMethod(), args);
    }

    public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
    }
}