package cn.x5456.summer.beans;

import java.util.List;

/**
 * 默认实现（Spring 中有两个实现 RootBD 和 ChildBD 其中 ChildBD 我们基本用不到）
 *
 * @author yujx
 * @date 2020/04/14 15:05
 */
public class DefaultBeanDefinition implements BeanDefinition {

    // 名称
    private String name;

    // 全类名
    private String className;

    // 生命周期
    private ScopeEnum scope = ScopeEnum.SINGLETON;

    private String initMethod;

    private String destroyMethod;

    // 工厂类注册到容器中的 beanName
    private String factoryBean;

    // 工厂方法名
    private String factoryMethod;

    // 属性参数列表
    private List<PropertyArgDefinition> properties;

    /**
     * 名称
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 类名称
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * 设置类名称
     */
    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public ScopeEnum getScope() {
        return scope;
    }

    @Override
    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }

    @Override
    public String getInitMethod() {
        return initMethod;
    }

    @Override
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    @Override
    public String getDestroyMethod() {
        return destroyMethod;
    }

    @Override
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    @Override
    public String getFactoryBean() {
        return factoryBean;
    }

    @Override
    public void setFactoryBean(String factoryBean) {
        this.factoryBean = factoryBean;
    }

    @Override
    public String getFactoryMethod() {
        return factoryMethod;
    }

    @Override
    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    @Override
    public List<PropertyArgDefinition> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(List<PropertyArgDefinition> properties) {
        this.properties = properties;
    }
}
