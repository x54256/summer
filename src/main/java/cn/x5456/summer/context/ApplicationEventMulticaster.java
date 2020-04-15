package cn.x5456.summer.context;

/**
 * 保存监听器的容器，当事件触发时调用他们
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加事件监听器
     */
    void addApplicationListener(ApplicationListener<?> l);

    /**
     * 删除事件监听器
     */
    void removeApplicationListener(ApplicationListener<?> l);

    /**
     * 删除所有的事件监听器
     */
    void removeAllListeners();

    /**
     * 广播事件
     */
    void multicastEvent(ApplicationEvent event);
}