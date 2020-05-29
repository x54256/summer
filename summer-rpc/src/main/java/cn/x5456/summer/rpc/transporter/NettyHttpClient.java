package cn.x5456.summer.rpc.transporter;


import cn.hutool.core.util.URLUtil;
import cn.x5456.summer.core.util.JsonUtils;
import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Result;
import cn.x5456.summer.rpc.RpcResult;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yujx
 * @date 2020/05/29 15:31
 */
public class NettyHttpClient implements Client {

    private String url;

    private ChannelHandler handler;

    public NettyHttpClient(String url, ChannelHandler handler) {
        this.url = url;
        this.handler = handler;
    }

    // 发送 http 请求
    @SneakyThrows
    @Override
    public Result sent(Invocation invocation) {

        Request request = new Request.Builder()
                .url("http://127.0.0.1:20880/?query=" + URLUtil.encode(JsonUtils.toString(invocation)))
                .build();

        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();
        System.out.println(resp);
        return JsonUtils.toBean(resp, RpcResult.class);
    }
}
