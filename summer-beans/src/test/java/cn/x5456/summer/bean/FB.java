package cn.x5456.summer.bean;

import cn.x5456.summer.beans.factory.FactoryBean;

/**
 * @author yujx
 * @date 2020/05/15 13:13
 */
public class FB implements FactoryBean<Apple> {
    @Override
    public Apple getObject() {
        return new Apple();
    }

    @Override
    public Class<?> getObjectType() {
        return Apple.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
