package cn.x5456.summer.rpc.test;

import cn.x5456.summer.beans.factory.annotation.Component;
import cn.x5456.summer.rpc.config.summer.Reference;

/**
 * @author yujx
 * @date 2020/05/20 14:59
 */
@Component
public class Consumer {

    // 这个地方名字不能和标注 @Service 注解的对象名字一样，这样就是直接从容器中取出来的
    @Reference
    private Provider provider1;

    public void test() {
        System.out.println(provider1.func());
        System.out.println(provider1.haveArgs(1, 2));
    }
}
