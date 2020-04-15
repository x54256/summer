package cn.x5456.summer.beans.factory;

import cn.x5456.summer.beans.ApplicationEvent;

import java.util.EventListener;

/**
 * Application 监听器（观察者模式）
 *
 * @author yujx
 * @date 2020/04/15 11:10
 */
public interface ApplicationListener<T extends ApplicationEvent> extends EventListener {

    /**
     * 处理事件的方法
     */
    void onApplicationEvent(T e);
}
