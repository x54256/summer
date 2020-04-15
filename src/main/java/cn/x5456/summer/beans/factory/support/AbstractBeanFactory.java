package cn.x5456.summer.beans.factory.support;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责维护单例对象（在 Spring 源码中该类还负责维护 parent 的bf，应该是为了 Spring MVC）
 *
 * @author yujx
 * @date 2020/04/14 19:14
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    // key：名字 value：单例对象
    private final Map<String, Object> sharedInstanceCache = new ConcurrentHashMap<>();

    /**
     * 根据名称获取对应的 bean （工厂方法模式）
     */
    @Override
    public Object getBean(String name) {
        if (sharedInstanceCache.containsKey(name)) {
            return sharedInstanceCache.get(name);
        }

        // 获取 bean 的属性信息
        BeanDefinition beanDefinition = this.getBeanDefinition(name);
        if (ObjectUtil.isNull(beanDefinition)) {
            throw new RuntimeException("获取的bean不存在！");
        }

        Object bean = this.createBean(beanDefinition);
        // 如果该对象是单例的，则加入到缓存中。
        if (beanDefinition.getScope().equals(BeanDefinition.ScopeEnum.SINGLETON)) {
            sharedInstanceCache.put(name, bean);
        }
        return bean;
    }

    // 根据 bd 创建对象
    private Object createBean(BeanDefinition beanDefinition) {
        return ReflectUtil.newInstance(beanDefinition.getClassName());
    }


    /**
     * 根据名称和它的类型获取对应的 bean
     */
    public <T> T getBean(String name, Class<T> requiredType) {
        Object bean = getBean(name);
        if (requiredType.isAssignableFrom(bean.getClass())) {
            return (T) bean;
        }
        throw new RuntimeException("获取bean的类型错误！");
    }

    /**
     * 根据名称获取 bd （子类实现）
     *
     * @param beanName
     * @return
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
