package cn.x5456.summer;

import cn.x5456.summer.beans.factory.ListableBeanFactory;

public interface BeanFactoryPostProcessor {

    /**
     * 在工厂实例化后做些什么
     */
    void postProcessBeanFactory(ListableBeanFactory beanFactory);

}
