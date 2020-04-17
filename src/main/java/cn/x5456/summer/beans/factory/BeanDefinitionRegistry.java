package cn.x5456.summer.beans.factory;

import cn.x5456.summer.beans.BeanDefinition;

/**
 * @author yujx
 * @date 2020/04/17 09:37
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册 bd 到 bf
     */
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);
}
