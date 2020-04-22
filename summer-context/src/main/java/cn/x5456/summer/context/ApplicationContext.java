package cn.x5456.summer.context;

import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.context.event.ApplicationEvent;
import cn.x5456.summer.core.env.Environment;

/**
 * 应用上下文接口（Spring 0.9 中还继承了一个国际化相关的接口）
 *
 * @author yujx
 * @date 2020/04/15 09:48
 */
public interface ApplicationContext extends ListableBeanFactory {

    /**
     * 返回父级上下文；如果没有父级，则返回null
     */
    ApplicationContext getParent();

    /**
     * 加载或刷新配置文件（例如 XML、JSON）
     */
    void refresh();

    /**
     * 触发一个事件
     */
    void publishEvent(ApplicationEvent event);

    /**
     * 关闭容器
     */
    void close();

    /**
     * 获取容器的环境
     */
    Environment getEnvironment();

    void addApplicationListener(ApplicationListener<?> listener);
}