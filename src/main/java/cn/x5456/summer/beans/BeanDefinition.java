package cn.x5456.summer.beans;

/**
 * bean 的属性
 *
 * @author yujx
 * @date 2020/04/14 14:57
 */
public interface BeanDefinition {

    /**
     * 名称
     */
    String getName();

    /**
     * 设置名称
     */
    void setName(String name);

    /**
     * 类名称
     */
    String getClassName();

    /**
     * 设置类名称
     */
    void setClassName(String className);

    ScopeEnum getScope();

    void setScope(ScopeEnum scope);

    String getInitMethod();

    void setInitMethod(String initMethod);

    String getDestroyMethod();

    void setDestroyMethod(String destroyMethod);

    String getFactoryBean();

    void setFactoryBean(String factoryBean);

    String getFactoryMethod();

    void setFactoryMethod(String factoryMethod);

    enum ScopeEnum {
        SINGLETON,
        PROTOTYPE
    }
}
