package cn.x5456.summer.context.support;

import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.beans.factory.config.BeanDefinition;
import cn.x5456.summer.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import cn.x5456.summer.beans.factory.config.BeanFactoryPostProcessor;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationListener;
import cn.x5456.summer.context.event.ApplicationEvent;
import cn.x5456.summer.context.event.ApplicationEventMulticaster;
import cn.x5456.summer.context.event.ApplicationEventMulticasterImpl;
import cn.x5456.summer.context.event.ContextRefreshedEvent;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.core.env.StandardEnvironment;

import java.util.Map;

;

/**
 * 这个类采用了模版方法模式，定义了 ApplicationContext 的初始化流程，
 * 它留下了2个方法 refreshBeanFactory() 和 getBeanFactory() 由子类实现
 * <p>
 * 注意：ListableBeanFactory 接口的方法，不会考虑父容器
 *
 * @author yujx
 * @date 2020/04/15 10:39
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    // 父容器（这里和父bf有啥关系）
    private ApplicationContext parent;

    // 事件广播器
    private ApplicationEventMulticaster eventMulticaster = new ApplicationEventMulticasterImpl();

    // 关闭钩子，防止意外关闭
    private Thread shutdownHook = new Thread(AbstractApplicationContext.this::doClose);

    private Environment environment = new StandardEnvironment();

    /**
     * 返回父级上下文；如果没有父级，则返回null
     */
    @Override
    public ApplicationContext getParent() {
        return parent;
    }

    /**
     * 加载或刷新配置文件（例如 XML、JSON）
     * <p>
     * 模版方法模式
     */
    @Override
    public void refresh() {

        // 初始化 BF，生成 BF，好在调用 getBeanFactory() 时返回
        this.refreshBeanFactory();

        // 执行 BeanDefinitionRegistryPostProcessor
        this.invokeBeanDefinitionRegistryPostProcessors();

        // 执行 BeanFactoryPostProcessor
        this.invokeBeanFactoryPostProcessors();

        // 向 beanPostProcessors 中添加后置处理器
        for (BeanPostProcessor beanPostProcessor : this.getBeansOfType(BeanPostProcessor.class).values()) {
            this.addBeanPostProcessor(beanPostProcessor);
        }

        // 初始化单例对象，如果实现了 ApplicationContextAware 则为其注入应用上下文
        this.configureAllManagedObjects();

        // 初始化 Listener
        this.refreshListeners();

        // 留给子类在刷新时用的钩子（空实现）
        this.onRefresh();

        // 触发事件
        this.publishEvent(new ContextRefreshedEvent(this));

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private void invokeBeanFactoryPostProcessors() {
        ListableBeanFactory beanFactory = this.getBeanFactory();

        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void invokeBeanDefinitionRegistryPostProcessors() {
        ListableBeanFactory beanFactory = this.getBeanFactory();

        for (BeanDefinitionRegistryPostProcessor beanDefinitionRegistryPostProcessor : beanFactory.getBeansOfType(BeanDefinitionRegistryPostProcessor.class).values()) {
            beanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry(beanFactory);
        }
    }

    /**
     * 初始化当前 ApplicationContext 的 BF
     */
    protected abstract void refreshBeanFactory();

    /**
     * 初始化单例对象，如果实现了 ApplicationContextAware 则为其注入应用上下文
     */
    private void configureAllManagedObjects() {
        String[] beanDefinitionNames = this.getBeanDefinitionNames();
        for (String bdName : beanDefinitionNames) {
            // 如果是单例，则对其初始化
            if (this.getBeanFactory().isSingleton(bdName)) {
                // 这句话会将其加入单例的 Map 中
                Object bean = this.getBeanFactory().getBean(bdName);
//                this.configureManagedObject(bean);
            }
        }
    }

    /**
     * 刷新 listener，将listener注册到事件广播器
     */
    private void refreshListeners() {
        Map<String, ApplicationListener> beansOfType = this.getBeansOfType(ApplicationListener.class);
        for (ApplicationListener listener : beansOfType.values()) {
            eventMulticaster.addApplicationListener(listener);
        }
    }

    protected void onRefresh() {
        // For subclasses: 默认情况下不执行任何操作。
    }

    /**
     * 通知事件监听器触发了一个事件
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        eventMulticaster.multicastEvent(event);
        if (parent != null) {
            parent.publishEvent(event);
        }
    }

    /**
     * 关闭容器
     */
    @Override
    public void close() {
        // 执行关闭操作
        this.doClose();

        // 移除关闭钩子
        if (ObjectUtil.isNotNull(shutdownHook)) {
            Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
        }
    }

    private void doClose() {
        // 销毁单例的 bean
        this.getBeanFactory().destroySingletons();
    }

    /**
     * 获取容器的环境
     */
    @Override
    public Environment getEnvironment() {
        return environment;
    }

    // ------> ListableBeanFactory 接口的方法，不会考虑父容器（我也不知道它为啥不考虑，搞不懂）

    /**
     * 获取容器中所有的 bd 的 name
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return this.getBeanFactory().getBeanDefinitionNames();
    }

    /**
     * 根据类型获取容器中所有这个类型的名字
     */
    @Override
    public String[] getBeanDefinitionNames(Class<?> type) {
        return this.getBeanFactory().getBeanDefinitionNames(type);
    }

    /**
     * 根据类型获取容器中所有该类型的对象
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> beansOfType = this.getBeanFactory().getBeansOfType(type);
//        for (T bean : beansOfType.values()) {
//            this.configureManagedObject(bean);
//        }
        return beansOfType;
    }

    // ------> BeanFactory 接口的方法，会考虑父容器

    /**
     * 根据名称获取对应的 bean（工厂方法模式）
     */
    @Override
    public Object getBean(String name) {
        Object bean = this.getBeanFactory().getBean(name);
//        this.configureManagedObject(bean);
        return bean;
    }
//
//    // 调用实现了 ApplicationContextAware 的 setApplicationContext 方法，注入应用上下文
//    private void configureManagedObject(Object o) {
//        if (o instanceof ApplicationContextAware) {
//            ApplicationContextAware aca = (ApplicationContextAware) o;
//            aca.setApplicationContext(this);
//        }
//    }

    /**
     * 根据名称和它的类型获取对应的 bean
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        T bean = this.getBeanFactory().getBean(name, requiredType);
//        this.configureManagedObject(bean);
        return bean;
    }

    /**
     * 根据名称获取它是否是单例（直接朋友，最少知道原则）
     */
    @Override
    public boolean isSingleton(String name) {
        return this.getBeanFactory().isSingleton(name);
    }

    // ------> BeanDefinitionRegistry

    /**
     * 注册 bd 到 bf
     */
    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.getBeanFactory().registerBeanDefinition(name, beanDefinition);
    }

    /**
     * 根据 bdName 获取 bd
     */
    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.getBeanFactory().getBeanDefinition(name);
    }

    // ------> 自定义方法，留着子类调用/实现

    protected void setParent(ApplicationContext ac) {
        this.parent = ac;
    }

    /**
     * 获取当前 ApplicationContext 的 BF
     */
    protected abstract ListableBeanFactory getBeanFactory();

    // ------>

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);
    }
}
