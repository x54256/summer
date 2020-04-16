package cn.x5456.summer;

import cn.x5456.summer.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * @author yujx
 * @date 2020/04/14 15:40
 */
public class Apple implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBean");
    }

    public void init() {
        System.out.println("init-method");
    }

    @PostConstruct
    public void func() {
        System.out.println("@PostConstruct");
    }

    @PostConstruct
    public void func2() {
        System.out.println("@PostConstruct2");
    }

    private String name = "红富士";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "name='" + name + '\'' +
                '}';
    }
}
