package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Result;

import java.util.Map;

/**
 * 该执行器的作用就是从 map 中取出服务提供者的发布器，然后通过发布器获取到执行器，执行执行器
 *
 * @author yujx
 * @date 2020/05/20 11:53
 */
public class InjvmInvoker<T> extends AbstractInvoker<T> {

    private final String key;

    private final Map<String, Exporter<?>> exporterMap;

    public InjvmInvoker(Class<T> type, String url, String serviceKey, Map<String, Exporter<?>> exporterMap) {
        super(type, url);
        this.key = serviceKey;
        this.exporterMap = exporterMap;
    }


    @Override
    protected Result doInvoke(Invocation invocation) {
        Exporter<?> exporter = exporterMap.get(key);
        return exporter.getInvoker().invoke(invocation);
    }

}
