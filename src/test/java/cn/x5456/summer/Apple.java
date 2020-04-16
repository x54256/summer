package cn.x5456.summer;

import cn.x5456.summer.beans.factory.DisposableBean;
import cn.x5456.summer.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author yujx
 * @date 2020/04/14 15:40
 */
public class Apple implements InitializingBean, DisposableBean {

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

    @Override
    public void destroy() {
        System.out.println("DisposableBean");
    }

    public void destroyMethod() {
        System.out.println("destroyMethod");
    }

    @PreDestroy
    public void func3() {
        System.out.println("@PreDestroy");
    }

}
