package cn.x5456.summer.rpc.proxy;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.Result;
import cn.x5456.summer.rpc.RpcResult;

/**
 * 这个是给被调用方执行的执行器
 * <p>
 * InvokerWrapper
 */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private T proxy;

    private Class<T> type;

    private String url;


    public AbstractProxyInvoker(Class<T> type, String url) {
        this.type = type;
        this.url = url;
    }

    public AbstractProxyInvoker(T proxy, Class<T> type, String url) {
        this.proxy = proxy;
        this.type = type;
        this.url = url;
    }

    @Override
    public Class<T> getInterface() {
        return type;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Result invoke(Invocation invocation) {
        return new RpcResult(doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments);

    @Override
    public String toString() {
        return getInterface() + " -> " + (getUrl() == null ? " " : getUrl().toString());
    }


}
