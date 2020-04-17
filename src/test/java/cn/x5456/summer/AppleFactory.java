package cn.x5456.summer;

import javax.annotation.PostConstruct;

/**
 * @author yujx
 * @date 2020/04/16 15:03
 */
public class AppleFactory {

    public Apple createApple() {
        Apple apple = new Apple();
        apple.setName("黄元帅");
        return apple;
    }

    public AppleFactory() {
        System.out.println(123123);
    }

    @PostConstruct
    public void init() {
        System.out.println("AppleFactory...");
    }
}
