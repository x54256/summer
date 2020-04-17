package cn.x5456.summer;

import cn.x5456.summer.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 * @author yujx
 * @date 2020/04/16 15:03
 */
public class AppleFactory implements BeanNameAware, ApplicationContextAware {

    public Apple createApple() {
        Apple apple = new Apple();
        apple.setName("黄元帅");
        return apple;
    }

    public AppleFactory() {
    }

    @PostConstruct
    public void init() {
        System.out.println("AppleFactory...");
    }

    /**
     * 注入 ApplicationContext
     *
     * @param ctx
     */
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        System.out.println("ctx = " + ctx);
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("name = " + name);
    }
}
