package cn.x5456.summer.rpc.proxy;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Invoker;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yujx
 * @date 2020/05/20 14:47
 */
public class RpcInvocation implements Invocation, Serializable {

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private transient Invoker<?> invoker;

    // 附件信息
    private Map<String, String> attachments = new HashMap<>();

    public RpcInvocation(Method method, Object[] args) {
        this.methodName = method.getName();
        this.parameterTypes = method.getParameterTypes();
        this.arguments = args;
    }

    public RpcInvocation(Method method, Object[] args, Map<String, String> attachments) {
        this(method, args);
        this.attachments.putAll(attachments);
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

    /**
     * get attachments.
     */
    @Override
    public Map<String, String> getAttachments() {
        return attachments;
    }

    /**
     * get attachment by key.
     */
    @Override
    public String getAttachment(String key) {
        return attachments.get(key);
    }

    /**
     * get attachment by key with default value.
     */
    @Override
    public String getAttachment(String key, String defaultValue) {
        return attachments.getOrDefault(key, defaultValue);
    }

    public void setAttachments(String k, String v) {
        this.attachments.put(k, v);
    }

    @Override
    public Invoker<?> getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker<?> invoker) {
        this.invoker = invoker;
    }
}
