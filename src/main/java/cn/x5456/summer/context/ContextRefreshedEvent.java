package cn.x5456.summer.context;

/**
 * 当ApplicationContext初始化或刷新时引发的事件。
 *
 * @author yujx
 * @date 2020/04/15 12:29
 */
public class ContextRefreshedEvent extends ApplicationEvent {

    public ContextRefreshedEvent(ApplicationContext source) {
        super(source);
    }

    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) super.getSource();
    }
}
