package cn.x5456.summer.beans.factory.config;

/**
 * @author yujx
 * @date 2020/04/19 15:46
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 后置处理注入属性
     */
    void postProcessPropertyValues(Object bean, String beanName);
}
