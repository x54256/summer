package cn.x5456.summer.beans.factory.support;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.BeanWrapper;
import cn.x5456.summer.beans.PropertyArgDefinition;
import cn.x5456.summer.beans.PropertyValue;
import cn.x5456.summer.beans.factory.*;
import cn.x5456.summer.beans.factory.config.BeanDefinition;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.x5456.summer.core.env.PropertyResolver;
import cn.x5456.summer.core.util.ReflectUtils;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
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

    /*
     假如 RefBean 是一个 FactoryBean 子类，他的 beanName 设置为 refBean

     那么在这个缓存中 key 是 refBean value 是 RefBean 对象

     FactoryBean#getObject 返回的对象存在 FactoryBeanRegistrySupport#factoryBeanObjectCache 中 格式为：{refBean: Proxy 对象}
     */
    // key：名字 value：单例对象
    private final Map<String, Object> sharedInstanceCache = new ConcurrentHashMap<>();

    // 二级缓存：维护早期暴露的Bean(只进行了实例化，并未进行属性注入)
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    // 销毁 注册中心
    private final DefaultSingletonBeanRegistry registry = new DefaultSingletonBeanRegistry();

    // 存放 bean 后置处理器列表
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    // 存放解析 @Value 注解的解析器
    private final List<PropertyResolver> embeddedValueResolvers = new ArrayList<>();

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
        // 如果一级缓存中没有，则从二级缓存中获取（防止循环引用）
        // 如果二级缓存中没有，则获取 bd 创建
        //      1. & 开头，将 name 删除 &；获取 FB 对象，判断是否是 FB 类型，不是报错，清除2级缓存；是否单例；然后返回
        //      2. 非 & 开头，直接获取，如果是 FB 类型，调用它的 getObj 方法，之后加入一级缓存


        // 先从一级缓存中获取
        if (sharedInstanceCache.containsKey(name)) {
            return sharedInstanceCache.get(name);
        }

        // 如果一级缓存中没有，则从二级缓存中获取（防止循环引用）
        if (earlySingletonObjects.containsKey(name)) {
            return earlySingletonObjects.get(name);
        }

        // 如果二级缓存中没有，则获取 bd 创建
        // 如果是 FactoryBean 类型
        if (this.isFactoryBean(name)) {

            // 不带 & 的 beanName
            String beanName = name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX) ? name.substring(1) : name;

            BeanDefinition bd = this.getBeanDefinition(beanName);
            if (ObjectUtil.isNotNull(bd)) {
                Object bean = this.createBean(bd);

                // 如果不是 FB 类型则报错
                if (!(bean instanceof FactoryBean)) {
                    // 清除二级缓存
                    earlySingletonObjects.remove(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
                    // 抛出异常
                    throw new RuntimeException("获取的 bean 不是 FactoryBean 类型，请不要使用 & 作为 BeanName");
                } else {
                    // 判断是否是单例，如果是则将 FB 对象加入一级缓存
                    if (bd.getScope() == BeanDefinition.ScopeEnum.SINGLETON) {
                        sharedInstanceCache.put(BeanFactory.FACTORY_BEAN_PREFIX + bd.getName(), bean);
                    }

                    // 如果 name 是以 & 开头，则直接返回对象
                    if (name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
                        return bean;
                    } else {
                        // 否则，调用它的 getObject() 方法获取对象，如果单例加入一级缓存
                        FactoryBean<?> fb = (FactoryBean<?>) bean;
                        Object obj = fb.getObject();
                        if (fb.isSingleton()) {
                            sharedInstanceCache.put(beanName, obj);
                        }
                        return obj;
                    }
                }
            }
        } else {
            // 否则，是普通对象
            BeanDefinition bd = this.getBeanDefinition(name);
            if (ObjectUtil.isNotNull(bd)) {
                Object bean = this.createBean(bd);
                // 判断是否是单例，如果是则加入一级缓存中
                if (bd.getScope() == BeanDefinition.ScopeEnum.SINGLETON) {
                    sharedInstanceCache.put(bd.getName(), bean);
                }
                return bean;
            }
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

    private boolean isFactoryBean(String beanName) {

        // 判断是否以 & 开头
        if (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            return true;
        }

        // 判断类型是否是 FB 的子类
        BeanDefinition bd = this.getBeanDefinition(beanName);
        if (ObjectUtil.isNull(bd)) {
            throw new RuntimeException(beanName + "的 bd 为空！");
        }

        Class<?> beanType;
        String className = bd.getClassName();
        if (ObjectUtil.isNotEmpty(className)) {
            beanType = ReflectUtils.getType(className);
        } else {
            beanType = this.factoryMethodReturnType(bd);
        }

        return FactoryBean.class.isAssignableFrom(beanType);
    }

    protected Class<?> factoryMethodReturnType(BeanDefinition bd) {
        String factoryBeanName = bd.getFactoryBean();
        String factoryMethod = bd.getFactoryMethod();

        BeanDefinition factoryBD = this.getBeanDefinition(factoryBeanName);
        String factoryClassName = factoryBD.getClassName();
        if (ObjectUtil.isNotEmpty(factoryBD)) {
            Method method = ClassUtil.getDeclaredMethod(ReflectUtils.getType(factoryClassName), factoryMethod);
            return method.getReturnType();
        } else {
            return this.factoryMethodReturnType(bd);
        }
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

            // 需要判断这个对象是不是 FB，如果是，加入二级缓存的时候要加 &符号
            String name = beanDefinition.getName();
            if (isFactoryBean(name)) {
                name = BeanFactory.FACTORY_BEAN_PREFIX + name;
            }
            earlySingletonObjects.put(name, beanWrapper.getWrappedInstance());
        }

        // 2、注入属性 DI （Spring 0.9 中当属性改变时会触发事件，但是默认是关闭的，暂时不知道它为了干啥）
        List<PropertyArgDefinition> properties = beanDefinition.getProperties();
        List<PropertyValue> propertyValueList = this.parseProperties(properties);
        beanWrapper.setPropertyValues(propertyValueList);
        Object bean = beanWrapper.getWrappedInstance();

        // 增加对注解的处理（在Spring中，下面这些是在 beanWrapper.setPropertyValues(propertyValueList); 之后执行的，为了避免重复注入）
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(bean, beanDefinition.getName());
            }
        }

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
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
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

    @Override
    public void addEmbeddedValueResolver(PropertyResolver propertyResolver) {
        embeddedValueResolvers.add(propertyResolver);
    }

    @Override
    public List<PropertyResolver> getEmbeddedValueResolvers() {
        return embeddedValueResolvers;
    }

    /**
     * 根据名称获取 bd （子类实现）
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);
}
