package cn.x5456.summer.beans.factory;

import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.core.env.PropertyResolver;

import java.util.List;

/**
 * 生产 bean 的工厂
 *
 * @author yujx
 * @date 2020/04/14 14:37
 */
public interface BeanFactory {

    /**
     * 根据名称获取对应的 bean（工厂方法模式）
     */
    Object getBean(String name);

    /**
     * 根据名称和它的类型获取对应的 bean
     */
    <T> T getBean(String name, Class<T> requiredType);

    /**
     * 根据名称获取它是否是单例（直接朋友，最少知道原则）
     */
    boolean isSingleton(String name);

    // ----> 下面这些默认方法都是 ConfigurableBeanFactory 这个接口中的，
    // 目的是让 ABF 实现，不让 AAP 实现

    /**
     * 执行单例对象销毁方法
     * <p>
     * 那么原型的怎么办？
     * <p>
     * 原型对象在获取的时候会执行初始化操作，且不会执行销毁操作
     * <p>
     * Note: 在 Spring5.0 中，这个方法在 ConfigurableBeanFactory 中扩展，但是如果新增这个接口
     * 会将类之间的逻辑变得十分复杂，有悖于我们做这个教程的初衷，所以采用 default 方法的方式，让 ABF
     * 实现这个接口，而不用 AAP 实现这个接口
     */
    default void destroySingletons() {
    }

    default void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
    }

    default void addEmbeddedValueResolver(PropertyResolver propertyResolver) {
    }

    default List<PropertyResolver> getEmbeddedValueResolvers() {
        return null;
    }
}
