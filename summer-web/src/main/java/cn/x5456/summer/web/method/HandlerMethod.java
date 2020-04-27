package cn.x5456.summer.web.method;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.core.MethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/04/24 09:34
 */
public class HandlerMethod {

    private final Object bean;

    private final BeanFactory beanFactory;

    private final Class<?> beanType;

    private final Method method;

    private final MethodParameter[] parameters;

    private final Class<?> returnType;

    public HandlerMethod(String beanName, Class<?> beanType, BeanFactory beanFactory, Method method) {
        this.bean = beanFactory.getBean(beanName);
        this.beanType = beanType;
        this.beanFactory = beanFactory;
        this.method = method;
        this.parameters = this.initMethodParameters();
        this.returnType = method.getReturnType();
    }

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.beanFactory = null;
        this.beanType = bean.getClass();
        this.method = method;
        this.parameters = initMethodParameters();
        this.returnType = method.getReturnType();
    }

    public HandlerMethod(HandlerMethod handlerMethod) {
        this.bean = handlerMethod.getBean();
        this.beanType = handlerMethod.getBeanType();
        this.beanFactory = handlerMethod.getBeanFactory();
        this.method = handlerMethod.getMethod();
        this.parameters = handlerMethod.getParameters();
        this.returnType = handlerMethod.getReturnType();
    }

    private MethodParameter[] initMethodParameters() {
        int count = this.method.getParameterTypes().length;
        MethodParameter[] result = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            result[i] = new MethodParameter(method, i);
        }
        return result;
    }

    public <T extends Annotation> T getMethodAnnotation(Class<T> clazz) {
        return AnnotationUtil.getAnnotation(method, clazz);
    }

    public boolean isVoid() {
        return Void.TYPE.equals(this.getReturnType());
    }

    public Object getBean() {
        return bean;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }
}
