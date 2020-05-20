package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invoker;

/**
 * Protocol. (API/SPI, Singleton, ThreadSafe)
 */
public interface Protocol {

    /**
     * Get default port when user doesn't config the port.
     *
     * @return default port
     */
    int getDefaultPort();

    /**
     * 用于远程调用的导出服务：
     * <p>
     * 1.协议在收到请求后应记录请求源地址：RpcContext.getContext（）。setRemoteAddress（）;
     * 2. export（）必须是幂等的，即在导出相同的URL时一次调用和两次调用之间没有区别。
     * 3.框架传入了Invoker实例，协议无需关心
     * <p>
     * Export service for remote invocation: <br>
     * 1. Protocol should record request source address after receive a request:
     * RpcContext.getContext().setRemoteAddress();<br>
     * 2. export() must be idempotent, that is, there's no difference between invoking once and invoking twice when
     * export the same URL<br>
     * 3. Invoker instance is passed in by the framework, protocol needs not to care <br>
     *
     * @param <T>     Service type
     * @param invoker Service invoker
     * @return exporter reference for exported service, useful for unexport the service later
     */
    <T> Exporter<T> export(Invoker<T> invoker);

    /**
     * Refer a remote service: <br>
     * 1. When user calls `invoke()` method of `Invoker` object which's returned from `refer()` call, the protocol
     * needs to correspondingly execute `invoke()` method of `Invoker` object <br>
     * 2. It's protocol's responsibility to implement `Invoker` which's returned from `refer()`. Generally speaking,
     * protocol sends remote request in the `Invoker` implementation. <br>
     * 3. When there's check=false set in URL, the implementation must not throw exception but try to recover when
     * connection fails.
     *
     * @param <T>  Service type
     * @param type Service class
     * @param url  URL address for the remote service
     * @return invoker service's local proxy
     */
    <T> Invoker<T> refer(Class<T> type, String url);

    /**
     * Destroy protocol: <br>
     * 1. Cancel all services this protocol exports and refers <br>
     * 2. Release all occupied resources, for example: connection, port, etc. <br>
     * 3. Protocol can continue to export and refer new service even after it's destroyed.
     */
    void destroy();

}