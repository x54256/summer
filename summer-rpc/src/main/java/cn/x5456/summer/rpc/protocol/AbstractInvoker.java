package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.Result;
import cn.x5456.summer.rpc.proxy.RpcInvocation;

public abstract class AbstractInvoker<T> implements Invoker<T> {

    private final Class<T> type;

    private final String url;

    public AbstractInvoker(Class<T> type, String url) {
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
    public String toString() {
        return getInterface() + " -> " + (getUrl() == null ? "" : getUrl().toString());
    }

    @Override
    public Result invoke(Invocation inv) {

        RpcInvocation invocation = (RpcInvocation) inv;
        invocation.setInvoker(this);
        return this.doInvoke(invocation);

    }

    protected abstract Result doInvoke(Invocation invocation);

}
