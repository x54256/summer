package cn.x5456.summer.rpc.proxy;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.Result;

/**
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
//        try {
//            return new RpcResult(doInvoke(proxy, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments()));
//        } catch (InvocationTargetException e) {
//            return new RpcResult(e.getTargetException());
//        } catch (Throwable e) {
//            throw new RpcException("Failed to invoke remote proxy method " + invocation.getMethodName() + " to " + getUrl() + ", cause: " + e.getMessage(), e);
//        }
        return null;
    }

    protected abstract Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable;

    @Override
    public String toString() {
        return getInterface() + " -> " + (getUrl() == null ? " " : getUrl().toString());
    }


}
