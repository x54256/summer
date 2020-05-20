package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invoker;

import java.util.Map;

/**
 * 本地的发布动作就是将其放进 exporterMap 中
 *
 * @author yujx
 * @date 2020/05/19 18:15
 */
public class InjvmExporter<T> implements Exporter<T> {

    private Invoker<T> invoker;

    private String key;

    public InjvmExporter(Invoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap) {
        this.invoker = invoker;
        this.key = key;

        exporterMap.put(key, this);
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }
}
