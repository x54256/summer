package cn.x5456.summer.rpc.proxy;

import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.rpc.Invoker;

/**
 * 我终于明白他为啥叫 Javassist ，他们里面的 Wrapper 对象是用 Javassist 写的实现
 *
 * @author yujx
 * @date 2020/05/19 16:41
 */
public class JavassistProxyFactory implements ProxyFactory {
    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return null;
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type, String url) {

        return new AbstractProxyInvoker<T>(proxy, type, url) {

            // FIXME: 2020/5/19 dubbo 的这个地方采用的是 Javassist 自己写的字节码
            @Override
            protected Object doInvoke(T proxy, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws Throwable {
                // 由于 Method#invoke() 是可变参数，所以我们这里没有办法传 arguments ，因为会把 Object[] 当做一个对象传进去
                // 所以我们只能做无参的。
                return ReflectUtil.invoke(proxy, methodName);
            }
        };
    }
}
