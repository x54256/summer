package cn.x5456.summer;

import cn.x5456.summer.context.ApplicationContext;

/**
 * 希望通过其运行在其中的应用程序上下文被通知的任何对象实现的接口。
 */
public interface ApplicationContextAware extends Aware {

    /**
     * 注入 ApplicationContext
     */
    void setApplicationContext(ApplicationContext ctx);

}
