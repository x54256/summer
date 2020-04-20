package cn.x5456.summer.stereotype;

import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.env.Environment;

/**
 * @author yujx
 * @date 2020/04/20 16:21
 */
public class ConditionContext {

    private BeanFactory beanFactory;

    private Environment environment;

    public ConditionContext(BeanFactory beanFactory, Environment environment) {
        this.beanFactory = beanFactory;
        this.environment = environment;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
