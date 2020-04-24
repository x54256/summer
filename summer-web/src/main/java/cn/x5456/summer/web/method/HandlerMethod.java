package cn.x5456.summer.web.method;

import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.core.MethodParameter;

import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/04/24 09:34
 */
public class HandlerMethod {

    private final String beanName;

    private final BeanFactory beanFactory;

    private final Class<?> beanType;

    private final Method method;

    private final MethodParameter[] parameters;

    public HandlerMethod(String beanName, Class<?> beanType, BeanFactory beanFactory, Method method) {
        this.beanName = beanName;
        this.beanType = beanType;
        this.beanFactory = beanFactory;
        this.method = method;
        this.parameters = this.initMethodParameters();
    }

    private MethodParameter[] initMethodParameters() {
        int count = this.method.getParameterTypes().length;
        MethodParameter[] result = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            result[i] = new MethodParameter(method, i);
        }
        return result;
    }
}
