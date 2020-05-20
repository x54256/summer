package cn.x5456.summer.rpc.proxy;

import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.protocol.Protocol;

/**
 * @author yujx
 * @date 2020/05/19 17:42
 */
public class ProtocolFilterWrapper implements Protocol {

    private final Protocol protocol;

    public ProtocolFilterWrapper(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public int getDefaultPort() {
        return protocol.getDefaultPort();
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) {
        // 构建责任链
        Invoker<T> invokerChain = this.buildInvokerChain(invoker);
        return protocol.export(invokerChain);
    }

    private <T> Invoker<T> buildInvokerChain(Invoker<T> invoker) {
        // 从容器中获取 Filter，拼成责任链，日后看心情写
        return invoker;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, String url) {
        return this.buildInvokerChain(protocol.refer(type, url));
    }

    @Override
    public void destroy() {
        protocol.destroy();
    }
}
