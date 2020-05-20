package cn.x5456.summer.rpc.proxy;

import cn.x5456.summer.rpc.Invoker;

/**
 * 代理工厂有两个功能
 *
 * @author yujx
 * @date 2020/05/19 16:39
 */
public interface ProxyFactory {

    /**
     * create proxy.
     * <p>
     * 根据 Invoker 创建代理对象
     */
    <T> T getProxy(Invoker<T> invoker);

    /**
     * create invoker.
     * <p>
     * 根据代理对象（也有可能是普通对象）创建 Invoker
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, String url);
}
