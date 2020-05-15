package cn.x5456.summer.beans.factory;

/**
 * 注意 getObject() 返回的对象是不会依赖注入的
 */
public interface FactoryBean<T> {

    /**
     * 返回此工厂管理的对象的实例（可能是共享的或独立的）
     */
    T getObject();

    /**
     * 返回此FactoryBean创建的对象的类型；如果事先未知，则返回{@code null}。
     *
     * @see ListableBeanFactory#getBeansOfType
     */
    Class<?> getObjectType();

    /**
     * 该工厂管理的对象是单例吗？也就是说，{@link #getObject()}是否总是返回同一对象（可以缓存的引用）
     */
    default boolean isSingleton() {
        return true;
    }

}
