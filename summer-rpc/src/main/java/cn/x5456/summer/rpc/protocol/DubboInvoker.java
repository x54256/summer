package cn.x5456.summer.rpc.protocol;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Result;
import cn.x5456.summer.rpc.proxy.RpcInvocation;
import cn.x5456.summer.rpc.transporter.Client;

/**
 * 调用端使用
 *
 * @author yujx
 * @date 2020/05/29 14:11
 */
public class DubboInvoker<T> extends AbstractInvoker<T> {

    private Client client;

    public DubboInvoker(Class<T> type, String url, Client client) {
        super(type, url);
        this.client = client;
    }

    @Override
    protected Result doInvoke(Invocation invocation) {

        // 获取接口名、端口号等信息设置到附件中
        String url = getUrl();

        int startIndex = url.replace("://", "").indexOf("/") + 4;
        int endIndex = url.indexOf("?");
        String interfaceName = url.substring(startIndex, endIndex);

        // 暂时写死了
        String port = "20880";

        ((RpcInvocation) invocation).setAttachments("path", interfaceName);
        ((RpcInvocation) invocation).setAttachments("port", port);

        // fixme: 2020/5/29 这里调用 client#request() 的一个方法，返回 future 对象（初步猜测应该是使用 netty 的监听器实现的）
//        return client.request(invocation);

        // 改成了 http 请求
        return client.sent(invocation);
    }
}
