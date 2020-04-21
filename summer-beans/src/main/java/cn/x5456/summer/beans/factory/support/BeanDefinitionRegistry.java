package cn.x5456.summer.beans.factory.support;

import cn.x5456.summer.beans.factory.config.BeanDefinition;

/**
 * @author yujx
 * @date 2020/04/17 09:37
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册 bd 到 bf
     */
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    /**
     * 根据 bdName 获取 bd
     */
    BeanDefinition getBeanDefinition(String name);
}
