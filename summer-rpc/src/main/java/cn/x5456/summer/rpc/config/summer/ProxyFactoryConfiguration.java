package cn.x5456.summer.rpc.config.summer;

import cn.x5456.summer.context.annotation.Bean;
import cn.x5456.summer.rpc.proxy.JavassistProxyFactory;

/**
 * @author yujx
 * @date 2020/05/20 09:16
 */
public class ProxyFactoryConfiguration {

    private static final String suffix = "ProxyFactory";

    @Bean(value = "injvm" + suffix)
    public JavassistProxyFactory javassistProxyFactory() {
        return new JavassistProxyFactory();
    }

}
