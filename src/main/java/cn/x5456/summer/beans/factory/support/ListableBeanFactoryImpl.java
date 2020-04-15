package cn.x5456.summer.beans.factory.support;

import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.BeanDefinition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ListableBeanFactory 工厂的实现，并负责维护 bd 信息
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
    protected final void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);

        // 将相同类型的beanName放入 allBeanNamesByType 中
        String className = beanDefinition.getClassName();
        Class<?> type = this.getType(className);
    }

    // 根据全类名获取类型
    private Class<?> getType(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
