package cn.x5456.summer.beans.factory;

/**
 * 生产 bean 的工厂
 *
 * @author yujx
 * @date 2020/04/14 14:37
 */
public interface BeanFactory {

    /**
     * 根据名称获取对应的 bean（工厂方法模式）
     */
    Object getBean(String name);

    /**
     * 根据名称和它的类型获取对应的 bean
     */
    <T> T getBean(String name, Class<T> requiredType);
}
