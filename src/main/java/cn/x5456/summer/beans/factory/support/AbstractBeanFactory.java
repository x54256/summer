package cn.x5456.summer.beans.factory.support;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.Aware;
import cn.x5456.summer.BeanNameAware;
import cn.x5456.summer.BeanPostProcessor;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.beans.BeanWrapper;
import cn.x5456.summer.beans.PropertyArgDefinition;
import cn.x5456.summer.beans.PropertyValue;
import cn.x5456.summer.beans.factory.*;
import cn.x5456.summer.util.ReflectUtils;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负责维护单例对象（在 Spring 源码中该类还负责维护 parent 的bf，应该是为了 Spring MVC）
 *
 * @author yujx
 * @date 2020/04/14 19:14
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    // 父 BF 为了应对多个 xml 配置文件的情况
    // 这是组合模式啊
    private BeanFactory parentBeanFactory;

    // key：名字 value：单例对象
    private final Map<String, Object> sharedInstanceCache = new ConcurrentHashMap<>();

    // 二级缓存：维护早期暴露的Bean(只进行了实例化，并未进行属性注入)
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    private final DefaultSingletonBeanRegistry registry = new DefaultSingletonBeanRegistry();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    /**
     * 根据名称获取对应的 bean （工厂方法模式）
     * <p>
     * 注：没有对原型做处理，如果2个对象都是原型，则会进入死循环从而堆栈溢出
     */
    @Override
    public Object getBean(String name) {

        // 先从一级缓存中获取
        if (sharedInstanceCache.containsKey(name)) {
            return sharedInstanceCache.get(name);
        }

        // 如果一级缓存中没有，则从二级缓存中获取（防止循环引用）
        if (earlySingletonObjects.containsKey(name)) {
            return earlySingletonObjects.get(name);
        }

        // 如果二级缓存中没有，则获取 bd 创建
        BeanDefinition bd = this.getBeanDefinition(name);
        if (ObjectUtil.isNotNull(bd)) {
            Object bean = this.createBean(bd);
            // 判断是否是单例，如果是则加入一级缓存中
            if (bd.getScope() == BeanDefinition.ScopeEnum.SINGLETON) {
                sharedInstanceCache.put(bd.getName(), bean);
            }
            return bean;
        }

        // 如果 bd 为空，而且没有父 bf，则抛出异常
        if (ObjectUtil.isNull(parentBeanFactory)) {
            throw new RuntimeException("获取的bean不存在！");
        }

        // 从父 bf 中获取
        return parentBeanFactory.getBean(name);


//        // -> 递归结束条件1
//        if (sharedInstanceCache.containsKey(name)) {
//            return sharedInstanceCache.get(name);
//        }
//
//        // 获取 bean 的属性信息
//        BeanDefinition beanDefinition = this.getBeanDefinition(name);
//        if (ObjectUtil.isNull(beanDefinition)) {
//            if (ObjectUtil.isNull(parentBeanFactory)) {
//                // -> 递归结束条件3
//                throw new RuntimeException("获取的bean不存在！");
//            } else {
//                // -> 这相当于递归
//                // 这里可以直接 return，因为已经再父 BF 中的缓存里了
//                return parentBeanFactory.getBean(name);
//            }
//        }
//
//        Object bean = this.createBean(beanDefinition);
//        // 如果该对象是单例的，则加入到缓存中。
//        if (beanDefinition.getScope().equals(BeanDefinition.ScopeEnum.SINGLETON)) {
//            sharedInstanceCache.put(name, bean);
//        }
//        // -> 递归结束条件2
//        return bean;
    }

    /**
     * 根据 bd 创建对象
     * <p>
     * 1）创建bean 日后需要对有参构造进行扩展
     * 2）注入属性（Spring 源码中 2 是在 3456 的后面）
     * 3）调用部分 Aware 的方法
     * 4）后置处理器的前置方法
     * 5）执行初始化操作
     * 6）后置处理器的后置方法
     * 7）注册销毁的处理
     */
    private Object createBean(BeanDefinition beanDefinition) {

        // 1、创建 bean
        BeanWrapper beanWrapper = this.createBeanInstance(beanDefinition);

        // 1.1 将还没有注入属性的 bean 放入二级缓存中
        if (beanDefinition.getScope() == BeanDefinition.ScopeEnum.SINGLETON) {
            earlySingletonObjects.put(beanDefinition.getName(), beanWrapper.getWrappedInstance());
        }

        // 2、注入属性 DI （Spring 0.9 中当属性改变时会触发事件，但是默认是关闭的，暂时不知道它为了干啥）
        List<PropertyArgDefinition> properties = beanDefinition.getProperties();
        List<PropertyValue> propertyValueList = this.parseProperties(properties);
        beanWrapper.setPropertyValues(propertyValueList);
        Object bean = beanWrapper.getWrappedInstance();

        // 3、调用部分 Aware 的方法
        this.invokeAwareMethod(bean, beanDefinition.getName());

        // 4、后置处理器的前置方法
        this.applyBeanPostProcessorsBeforeInitialization(bean, beanDefinition.getName());

        // 5、执行初始化操作（在 Spring 中是直接调用的该类中的 initializeBean 方法，为了让他面向对象一点，我给他抽出一个类）
        InitializeBeanAdapter initializeBeanAdapter = new InitializeBeanAdapter(bean, beanDefinition);
        initializeBeanAdapter.afterPropertiesSet();

        // 6、后置处理器的后置方法
        this.applyBeanPostProcessorsAfterInitialization(bean, beanDefinition.getName());

        // 7、注册销毁的处理
        if (this.check(beanDefinition, bean)) {
            registry.registerDisposableBean(beanDefinition.getName(), new DisposableBeanAdapter(bean, beanDefinition));
        }

        return bean;
    }

    private void invokeAwareMethod(Object bean, String beanName) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            // else if () ...
        }
    }

    // 后置方法
    private void applyBeanPostProcessorsAfterInitialization(Object bean, String name) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanPostProcessor.postProcessAfterInitialization(bean, name);
        }
    }

    // 前置方法
    private void applyBeanPostProcessorsBeforeInitialization(Object bean, String name) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanPostProcessor.postProcessBeforeInitialization(bean, name);
        }
    }

    private List<PropertyValue> parseProperties(List<PropertyArgDefinition> properties) {

        if (CollectionUtil.isEmpty(properties)) {
            return Collections.emptyList();
        }

        List<PropertyValue> propertyValueList = new ArrayList<>(properties.size());

        for (PropertyArgDefinition property : properties) {
            String propertyName = property.getName();
            String refBeanName = property.getRefName();
            if (ObjectUtil.isNotNull(refBeanName)) {
                Object refBean = this.getBean(refBeanName);
                PropertyValue propertyValue = new PropertyValue(propertyName, refBean);
                propertyValueList.add(propertyValue);
            } else {
                String value = property.getValue();
                try {
                    Class<?> clazz = Class.forName(property.getType());
                    PropertyValue propertyValue = new PropertyValue(propertyName, ReflectUtils.string2BasicType(value, clazz));
                    propertyValueList.add(propertyValue);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return propertyValueList;
    }

    /**
     * 分两种情况
     * <p>
     * 1. 工厂方法（无参版）
     * 2. 空参构造
     */
    private BeanWrapper createBeanInstance(BeanDefinition beanDefinition) {
        if (ObjectUtil.isNotNull(beanDefinition.getFactoryBean())) {
            // 从容器中找到工厂对象
            Object bean = this.getBean(beanDefinition.getFactoryBean());
            if (ObjectUtil.isNull(bean)) {
                throw new RuntimeException("工厂 bean 不存在！");
            }
            return new BeanWrapper(ReflectUtil.invoke(bean, beanDefinition.getFactoryMethod()));
        }

        return new BeanWrapper(ReflectUtil.newInstance(beanDefinition.getClassName()));
    }

    /**
     * 检查是否具有销毁方法
     */
    private boolean check(BeanDefinition bd, Object bean) {
        return ObjectUtil.isNotNull(bd.getDestroyMethod()) ||
                bean instanceof DisposableBean ||
                CollectionUtil.isNotEmpty(ReflectUtils.getMethodsByAnnotation(bean.getClass(), PreDestroy.class));
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
        BeanDefinition beanDefinition = this.getBeanDefinition(name);
        if (ObjectUtil.isNull(beanDefinition)) {
            if (ObjectUtil.isNull(parentBeanFactory)) {
                throw new RuntimeException("输入的name有误！");
            } else {
                // 没找到就去父 bf 中找
                return parentBeanFactory.isSingleton(name);
            }
        }
        return beanDefinition.getScope() == BeanDefinition.ScopeEnum.SINGLETON;
    }

    /**
     * 执行单例对象销毁方法
     * <p>
     * 那么原型的怎么办？
     * <p>
     * 原型对象在获取的时候会执行初始化操作，且不会执行销毁操作
     */
    @Override
    public void destroySingletons() {
        registry.destroySingletons();
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 根据名称获取 bd （子类实现）
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
