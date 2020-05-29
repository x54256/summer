package cn.x5456.summer.rpc.test;

import cn.x5456.summer.core.util.JsonUtils;
import cn.x5456.summer.rpc.config.summer.Service;
import cn.x5456.summer.rpc.proxy.RpcInvocation;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/05/20 14:59
 */
@Service
public class ProviderImpl implements Provider {
    @Override
    public Object func() {
        return "啦啦啦";
    }

    @Override
    public Integer haveArgs(Integer a, Integer b) {
        return a + b;
    }

    @SneakyThrows
    public static void main(String[] args) {
        Method method = Provider.class.getMethod("func");
        RpcInvocation rpcInvocation = new RpcInvocation(method, new Object[0]);

        rpcInvocation.setAttachments("path", "cn.x5456.summer.rpc.test.Provider");
        rpcInvocation.setAttachments("port", "20880");


        System.out.println(JsonUtils.toString(rpcInvocation));
    }
}
