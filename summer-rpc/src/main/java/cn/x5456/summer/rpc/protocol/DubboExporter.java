package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invoker;

import java.util.Map;

/**
 * @author yujx
 * @date 2020/05/29 10:26
 */
public class DubboExporter<T> implements Exporter<T> {

    private Invoker<T> invoker;

    private String key;

    public DubboExporter(Invoker<T> invoker, String serviceKey, Map<String, Exporter<?>> exporterMap) {
        this.invoker = invoker;
        this.key = serviceKey;
    }

    /**
     * get invoker.
     *
     * @return invoker
     */
    @Override
    public Invoker<T> getInvoker() {
        return null;
    }
}
