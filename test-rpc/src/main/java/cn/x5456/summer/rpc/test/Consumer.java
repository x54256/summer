package cn.x5456.summer.rpc.test;

import cn.x5456.summer.beans.factory.annotation.Component;
import cn.x5456.summer.rpc.config.summer.Reference;

/**
 * @author yujx
 * @date 2020/05/20 14:59
 */
@Component
public class Consumer {

    @Reference
    private Provider provider;

    public void test() {
        System.out.println(provider.func());
        System.out.println(provider.haveArgs(1, 2));
    }
}
