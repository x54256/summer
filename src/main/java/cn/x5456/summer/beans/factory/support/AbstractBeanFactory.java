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

    // 父 BF 为了应对多个 xml 配置文件的情况
    // 这是不是组合模式啊。
    private BeanFactory parentBeanFactory;

    // key：名字 value：单例对象
    private final Map<String, Object> sharedInstanceCache = new ConcurrentHashMap<>();

    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    /**
     * 根据名称获取对应的 bean （工厂方法模式）
     */
    @Override
    public Object getBean(String name) {

        // -> 递归结束条件1
        if (sharedInstanceCache.containsKey(name)) {
            return sharedInstanceCache.get(name);
        }

        // 获取 bean 的属性信息
        BeanDefinition beanDefinition = this.getBeanDefinition(name);
        if (ObjectUtil.isNull(beanDefinition)) {
            if (ObjectUtil.isNull(parentBeanFactory)) {
                // -> 递归结束条件3
                throw new RuntimeException("获取的bean不存在！");
            } else {
                // -> 这相当于递归
                // 这里可以直接 return，因为已经再父 BF 中的缓存里了
                return parentBeanFactory.getBean(name);
            }
        }

        Object bean = this.createBean(beanDefinition);
        // 如果该对象是单例的，则加入到缓存中。
        if (beanDefinition.getScope().equals(BeanDefinition.ScopeEnum.SINGLETON)) {
            sharedInstanceCache.put(name, bean);
        }
        // -> 递归结束条件2
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
     * 根据名称获取它是否是单例（直接朋友，最少知道原则）
     */
    @Override
    public boolean isSingleton(String name) {
        return this.getBeanDefinition(name).getScope() == BeanDefinition.ScopeEnum.SINGLETON;
    }

    /**
     * 根据名称获取 bd （子类实现）
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
