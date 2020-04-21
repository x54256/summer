package cn.x5456.summer.beans.factory;

/**
 * 注入 BeanName
 *
 * @author yujx
 * @date 2020/04/17 14:43
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);
}
