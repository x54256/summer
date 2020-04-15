package cn.x5456.summer;

import cn.x5456.summer.context.ApplicationListener;
import cn.x5456.summer.context.ContextRefreshedEvent;

/**
 * @author yujx
 * @date 2020/04/15 16:20
 */
public class TestListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * 处理事件的方法（每装配一个xml文件，就要调用一次）
     *
     * @param e
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {
        System.out.println(e);
    }

    /**
     * 获取事件类型
     */
    @Override
    public Class<ContextRefreshedEvent> getEventType() {
        return ContextRefreshedEvent.class;
    }
}
