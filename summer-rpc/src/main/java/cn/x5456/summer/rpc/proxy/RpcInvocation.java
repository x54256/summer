package cn.x5456.summer.rpc.proxy;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Invoker;

import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/05/20 14:47
 */
public class RpcInvocation implements Invocation {

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private transient Invoker<?> invoker;

    public RpcInvocation(Method method, Object[] args) {
        this.methodName = method.getName();
        this.parameterTypes = method.getParameterTypes();
        this.arguments = args;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Invoker<?> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
    }
}
