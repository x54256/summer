package cn.x5456.summer.web.method.support;

import cn.hutool.core.util.ArrayUtil;
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
     *
     * @param providedArgs 提供的参数值列表，因为有的参数是不能从请求和mavContainer中获取到的，就需要通过这个参数传入
     */
    public Object invokeForRequest(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs) {

        // 获取到当前方法的参数
        MethodParameter[] parameters = this.getParameters();

        // 使用参数解析器，将其从请求中取出
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {

            // 如果用户提供的参数列表不为空，则尝试从提供的参数值列表中解析方法参数
            if (ArrayUtil.isNotEmpty(providedArgs)) {
                for (Object providedArg : providedArgs) {
                    if (parameters[i].getParameterType().isInstance(providedArg)) {
                        args[i] = providedArg;
                        break;
                    }
                }
            }

            if (args[i] != null) {
                continue;
            }

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