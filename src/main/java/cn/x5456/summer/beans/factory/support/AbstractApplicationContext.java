package cn.x5456.summer.beans.factory.support;

import cn.x5456.summer.beans.*;
import cn.x5456.summer.beans.factory.ApplicationContext;
import cn.x5456.summer.beans.factory.ApplicationListener;
import cn.x5456.summer.beans.factory.ListableBeanFactory;

import java.util.Map;

/**
 * @author yujx
 * @date 2020/04/15 10:39
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    // 父容器（这里和父bf有啥关系）
    private ApplicationContext parent;

    // 事件广播器
    private ApplicationEventMulticaster eventMulticaster = new ApplicationEventMulticasterImpl();

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

        this.refreshBeanFactory();

        // 初始化单例对象，如果实现了 ApplicationContextAware 则为其注入应用上下文
        this.configureAllManagedObjects();

        // 初始化 Listener
        this.refreshListeners();

        // 留给子类在刷新时用的钩子（空实现）
        this.onRefresh();

        // 触发事件
        this.publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * 刷新 BF
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
                this.configureManagedObject(bean);
            }
        }
    }

    /**
     * 注册 listener
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
        for (T bean : beansOfType.values()) {
            this.configureManagedObject(bean);
        }
        return beansOfType;
    }

    // ------> BeanFactory 接口的方法，会考虑父容器

    /**
     * 根据名称获取对应的 bean（工厂方法模式）
     */
    @Override
    public Object getBean(String name) {
        Object bean = this.getBeanFactory().getBean(name);
        this.configureManagedObject(bean);
        return bean;
    }

    // 调用实现了 ApplicationContextAware 的 setApplicationContext 方法，注入应用上下文
    private void configureManagedObject(Object o) {
        if (o instanceof ApplicationContextAware) {
            ApplicationContextAware aca = (ApplicationContextAware) o;
            aca.setApplicationContext(this);
        }
    }

    /**
     * 根据名称和它的类型获取对应的 bean
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        T bean = this.getBeanFactory().getBean(name, requiredType);
        this.configureManagedObject(bean);
        return bean;
    }

    protected void setParent(ApplicationContext ac) {
        this.parent = ac;
    }

    /**
     * 获取 BF
     */
    protected abstract ListableBeanFactory getBeanFactory();
}
