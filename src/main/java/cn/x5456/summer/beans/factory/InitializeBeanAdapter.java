package cn.x5456.summer.beans.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.util.ReflectUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * 适配器模式，将 bean 适配成 InitializingBean
 *
 * @author yujx
 * @date 2020/04/16 10:19
 */
public class InitializeBeanAdapter implements InitializingBean {

    private Object bean;

    private BeanDefinition bd;

    public InitializeBeanAdapter(Object bean, BeanDefinition bd) {
        this.bean = bean;
        this.bd = bd;
    }


    /**
     * 设置所有提供的bean属性后，由BeanFactory调用。
     */
    @Override
    public void afterPropertiesSet() {

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
        for (Method method : ReflectUtils.getMethodsByAnnotation(bean.getClass(), PostConstruct.class)) {
            ReflectUtil.invoke(bean, method);
        }
    }
}
