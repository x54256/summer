package cn.x5456.summer.rpc.proxy;

import cn.hutool.aop.ProxyUtil;
import cn.x5456.summer.core.util.ReflectUtils;
import cn.x5456.summer.rpc.Invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 我终于明白他为啥叫 Javassist ，他们里面的 Wrapper 对象是用 Javassist 写的实现
 *
 * @author yujx
 * @date 2020/05/19 16:41
 */
public class JavassistProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return (T) ProxyUtil.newProxyInstance(new InvokerInvocationHandler(invoker), invoker.getInterface());
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, String url) {

        return new AbstractProxyInvoker<T>(proxy, type, url) {

            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
                return ReflectUtils.invokeMethod(proxy, methodName, parameterTypes, arguments);
            }
        };
    }

    private static class InvokerInvocationHandler implements InvocationHandler {

        private final Invoker<?> invoker;

        public <T> InvokerInvocationHandler(Invoker<T> invoker) {
            this.invoker = invoker;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            return invoker.invoke(new RpcInvocation(method, args)).recreate();
        }
    }
}
