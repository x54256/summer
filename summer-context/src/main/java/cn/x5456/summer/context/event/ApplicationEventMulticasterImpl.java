package cn.x5456.summer.context.event;

import cn.x5456.summer.context.ApplicationListener;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yujx
 * @date 2020/04/15 11:20
 */
public class ApplicationEventMulticasterImpl implements ApplicationEventMulticaster {

    private final Set<ApplicationListener<?>> listenerSet = new HashSet<>();

    /**
     * 添加事件监听器
     *
     * @param l
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> l) {
        listenerSet.add(l);
    }

    /**
     * 删除事件监听器
     *
     * @param l
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> l) {
        listenerSet.remove(l);
    }

    /**
     * 删除所有的事件监听器
     */
    @Override
    public void removeAllListeners() {
        listenerSet.clear();
    }

    /**
     * 广播事件
     *
     * @param event
     */
    @Override
    @SuppressWarnings("all")
    public void multicastEvent(ApplicationEvent event) {
        // 把 Listener 的泛型擦除了，才可以调用。。Java的泛型是真恶心
        for (ApplicationListener applicationListener : this.getApplicationListeners(event)) {
            applicationListener.onApplicationEvent(event);
        }
    }

    private Set<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event) {
        HashSet<ApplicationListener<?>> set = new HashSet<>();

        for (ApplicationListener<?> applicationListener : listenerSet) {
            if (applicationListener.getEventType().isAssignableFrom(event.getClass())) {
                set.add(applicationListener);
            }
        }
        return set;
    }
}
