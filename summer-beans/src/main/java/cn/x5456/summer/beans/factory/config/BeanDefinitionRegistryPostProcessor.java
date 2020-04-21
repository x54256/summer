package cn.x5456.summer.beans.factory.config;

import cn.x5456.summer.beans.factory.support.BeanDefinitionRegistry;

/**
 * 使我们可以在 BeanFactory 初始化好了之后，向其中注册 BD
 * <p>
 * 在 Spring 5.0 中它继承了 BeanFactoryPostProcessor 但是我觉得没必要
 */
public interface BeanDefinitionRegistryPostProcessor {

    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry);

}
