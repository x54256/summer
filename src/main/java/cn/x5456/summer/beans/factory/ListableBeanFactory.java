package cn.x5456.summer.beans.factory;

import java.util.Map;

/**
 * 扩展了bf，可以根据类型返回容器中所有的 bean
 * <p>
 * BeanFactory的扩展将由可以枚举其所有bean实例的bean工厂来实现，而不是按照客户的要求按名称一一尝试。
 * <p>
 * 注意：这个接口中的方法不会从父容器中获取数据的，使用时需要注意
 *
 * @author yujx
 * @date 2020/04/14 14:48
 */
public interface ListableBeanFactory extends BeanFactory, BeanDefinitionRegistry {

    /**
     * 获取容器中所有的 bd 的 name
     */
    String[] getBeanDefinitionNames();

    /**
     * 根据类型获取容器中所有这个类型的名字
     */
    String[] getBeanDefinitionNames(Class<?> type);

    /**
     * 根据类型获取容器中所有该类型的对象
     */
    <T> Map<String, T> getBeansOfType(Class<T> type);
}
