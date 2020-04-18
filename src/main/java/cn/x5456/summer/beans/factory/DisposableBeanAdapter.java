package cn.x5456.summer.beans.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.util.ReflectUtils;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/04/16 12:34
 */
public class DisposableBeanAdapter implements DisposableBean {

    private Object bean;

    private BeanDefinition bd;

    public DisposableBeanAdapter(Object bean, BeanDefinition bd) {
        this.bean = bean;
        this.bd = bd;
    }

    /**
     * 由BeanFactory在销毁bean时调用。
     */
    @Override
    public void destroy() {
        // 1、处理 @PreDestroy 注解，执行注解的方法（此处 Spring 是采用后置处理器【DestructionAwareBeanPostProcessor】实现的，为什么要采用这个实现，而不是直接调用呢？）
        this.postPreDestroy();

        // 2、如果该 bean 实现了 DisposableBean 接口，则调用
        this.disposableBean();

        // 3、处理用户在 xml 文件中配置的 destroy-method 方法
        this.customDestroy();
    }

    private void customDestroy() {
        String destroyMethod = bd.getDestroyMethod();
        if (ObjectUtil.isNotEmpty(destroyMethod)) {
            ReflectUtil.invoke(bean, destroyMethod);
        }
    }

    private void disposableBean() {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
    }

    private void postPreDestroy() {
        for (Method method : ReflectUtils.getMethodsByAnnotation(bean.getClass(), PreDestroy.class)) {
            ReflectUtil.invoke(bean, method);
        }
    }
}
