package cn.x5456.summer;

import cn.x5456.summer.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yujx
 * @date 2020/04/17 20:41
 */
@Component
public class A {

    @Resource
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
