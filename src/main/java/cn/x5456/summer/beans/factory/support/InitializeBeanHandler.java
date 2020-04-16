package cn.x5456.summer.beans.factory.support;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yujx
 * @date 2020/04/16 10:19
 */
public final class InitializeBeanHandler {

    private Object bean;

    private BeanDefinition bd;

    public InitializeBeanHandler(Object bean, BeanDefinition bd) {
        this.bean = bean;
        this.bd = bd;
    }

    public void initBean() {

        // 1、处理 @PostConstruct 注解，执行注解的方法（此处 Spring 是采用后置处理器实现的，为什么要采用这个实现，而不是直接调用呢？）
        this.postConstruct();

        // 2、如果该 bean 实现了 InitializingBean 接口，则调用
        this.initializingBean();

        // 3、处理用户在 xml 文件中配置的 init-method 方法
        this.customInit();
    }

    private void customInit() {
        String initMethod = bd.getInitMethod();
        if (ObjectUtil.isNotNull(initMethod)) {
            ReflectUtil.invoke(bean, initMethod);
        }
    }

    private void initializingBean() {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    private void postConstruct() {
        for (Method method : this.getMethodOptional(bean.getClass(), PostConstruct.class)) {
            ReflectUtil.invoke(bean, method);
        }
    }

    private List<Method> getMethodOptional(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        Method[] methods = tClass.getMethods();

        List<Method> matches = new ArrayList<>();
        if (!ArrayUtil.isEmpty(methods)) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotationClass)) {
                    matches.add(method);
                }
            }
        }
        return matches;
    }
}
