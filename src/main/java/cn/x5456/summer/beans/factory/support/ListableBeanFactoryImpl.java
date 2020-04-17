package cn.x5456.summer.beans.factory.support;

import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.beans.factory.ListableBeanFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ListableBeanFactory 工厂的实现，并负责维护 bd 信息
 * <p>
 * 注意：这个接口中的方法不会从父容器中获取数据的，使用时需要注意
 *
 * @author yujx
 * @date 2020/04/14 14:56
 */
public class ListableBeanFactoryImpl extends AbstractBeanFactory implements ListableBeanFactory {

    // key：名字 value：bean 的信息
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public ListableBeanFactoryImpl() {
    }

    public ListableBeanFactoryImpl(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    /**
     * 注册 bean 的信息
     */
    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

    /**
     * 获取容器中所有的 bd 的 name
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    /**
     * 根据类型获取容器中所有这个类型的名字
     */
    @Override
    public String[] getBeanDefinitionNames(Class<?> type) {
        // Spring 5.0 为了效率加了个缓存

        List<String> matches = new ArrayList<>();

        Set<String> beanNames = beanDefinitionMap.keySet();
        for (String beanName : beanNames) {
            Object bean = super.getBean(beanName);
            if (type.isAssignableFrom(bean.getClass())) {
                matches.add(beanName);
            }
        }

        return matches.toArray(new String[0]);
    }

    /**
     * 根据类型获取容器中所有该类型的对象
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        String[] beanNames = this.getBeanDefinitionNames(type);
        if (ObjectUtil.isNull(beanNames)) {
            return new HashMap<>(0);
        }

        Map<String, T> map = new HashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            map.put(beanName, this.getBean(beanName, type));
        }
        return map;
    }

    /**
     * 根据名称获取 bd （子类实现）
     *
     * @param beanName
     * @return
     */
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }
}
