package cn.x5456.summer;

import cn.x5456.summer.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yujx
 * @date 2020/04/17 20:42
 */
@Component
public class B {

    @Resource
    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
