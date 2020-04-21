package cn.x5456.summer.beans.factory;

/**
 * @author yujx
 * @date 2020/04/16 09:37
 */
public interface InitializingBean {

    /**
     * 设置所有提供的bean属性后，由BeanFactory调用。
     */
    void afterPropertiesSet();

}

