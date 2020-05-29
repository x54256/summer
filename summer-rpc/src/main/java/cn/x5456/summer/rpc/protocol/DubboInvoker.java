package cn.x5456.summer.rpc.protocol;

import cn.hutool.core.util.ReUtil;
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

        String port = ReUtil.getGroup0("bind.port=\\d+&", url).replace("bind.port=", "").replace("&", "");

        ((RpcInvocation) invocation).setAttachments("path", interfaceName);
        ((RpcInvocation) invocation).setAttachments("port", port);

//        return client.request(invocation);

    }
}
