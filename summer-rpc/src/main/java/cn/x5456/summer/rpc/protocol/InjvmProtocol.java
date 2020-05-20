package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invoker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yujx
 * @date 2020/05/19 17:57
 */
public class InjvmProtocol implements Protocol {

    private final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();

    @Override
    public int getDefaultPort() {
        return 0;
    }

    /**
     * 发布 Invoker
     */
    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) {
        // 使用发布器发布，serviceKey 就是接口名
        int startIndex = invoker.getUrl().replace("://", "").indexOf("/") + 4;
        int endIndex = invoker.getUrl().indexOf("?");
        String serviceKey = invoker.getUrl().substring(startIndex, endIndex);
        return new InjvmExporter<T>(invoker, serviceKey, exporterMap);
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, String url) {
        return null;
    }

    @Override
    public void destroy() {

    }
}
