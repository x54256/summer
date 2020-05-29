package cn.x5456.summer.rpc.protocol;

import cn.hutool.core.util.ReUtil;
import cn.x5456.summer.core.util.JsonUtils;
import cn.x5456.summer.rpc.Exporter;
import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Invoker;
import cn.x5456.summer.rpc.Result;
import cn.x5456.summer.rpc.transporter.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yujx
 * @date 2020/05/29 10:14
 */
public class DubboProtocol implements Protocol {

    private final Map<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();

    private final Map<String, Server> serverMap = new HashMap<>();

    private ChannelHandler requestHandler = new ChannelHandler() {
        @Override
        public void received(ChannelHandlerContext ctx, Invocation invocation) {
            // 通过 invocation 中的附件信息获取 serviceKey
            Map<String, String> attachments = invocation.getAttachments();

            // com.dubbo.DemoService
            String path = attachments.get("path");
            // 20880
            String port = attachments.get("port");

            // 根据 serviceKey 从 exporterMap 获取 Exporter
            String serviceKey = path + ":" + port;

            // 调用本地方法，然后写回调用者那方
            Exporter<?> exporter = exporterMap.get(serviceKey);
            if (exporter == null) {
                throw new RuntimeException("该 serviceKey 未发布");
            }

            Result result = exporter.getInvoker().invoke(invocation);
            ByteBuf content = Unpooled.copiedBuffer(JsonUtils.toString(result).getBytes(StandardCharsets.UTF_8));
            // 设置响应
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ChannelFuture channelFuture = ctx.writeAndFlush(response);
            ctx.close();
        }
    };


    /**
     * Get default port when user doesn't config the port.
     *
     * @return default port
     */
    @Override
    public int getDefaultPort() {
        return 20880;
    }

    /**
     * 用于远程调用的导出服务：
     * <p>
     * 1.协议在收到请求后应记录请求源地址：RpcContext.getContext（）。setRemoteAddress（）;
     * 2. export（）必须是幂等的，即在导出相同的URL时一次调用和两次调用之间没有区别。
     * 3.框架传入了Invoker实例，协议无需关心
     * <p>
     * Export service for remote invocation: <br>
     * 1. Protocol should record request source address after receive a request:
     * RpcContext.getContext().setRemoteAddress();<br>
     * 2. export() must be idempotent, that is, there's no difference between invoking once and invoking twice when
     * export the same URL<br>
     * 3. Invoker instance is passed in by the framework, protocol needs not to care <br>
     *
     * @param invoker Service invoker
     * @return exporter reference for exported service, useful for unexport the service later
     */
    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) {

        String url = invoker.getUrl();

        // 使用发布器发布，serviceKey 就是接口名 + 端口号
        int startIndex = url.replace("://", "").indexOf("/") + 4;
        int endIndex = url.indexOf("?");
        String port = ReUtil.getGroup0("bind.port=\\d+&", url).replace("bind.port=", "").replace("&", "");

        // com.dubbo.DemoService:20880
        String serviceKey = url.substring(startIndex, endIndex) + ":" + port;
        DubboExporter<T> exporter = new DubboExporter<>(invoker, serviceKey, exporterMap);

        // 如果还没有对外发布服务，对外发布服务
        String ip = "127.0.0.1"; // 直接写死了，理论上是从 url 上获取的

        String serverKey = ip + ":" + port;
        if (!serverMap.containsKey(serverKey)) {
            Server server = this.openServer(invoker.getUrl());
            serverMap.put(serverKey, server);
        }

        // 将发布器对象放进已发布 map 中
        exporterMap.put(serviceKey, exporter);

        return exporter;
    }

    private Server openServer(String url) {
        return this.createServer(url);
    }

    private Server createServer(String url) {
        // 这里我省略几步骤，因为我实在不知道中间那几步的目的是啥，我直接使用 NettyTransporter 创建 Netty 服务端了
        Transporter transporter = new NettyTransporter();
        return transporter.bind(url, requestHandler);
    }

    /**
     * Refer a remote service: <br>
     * 1. When user calls `invoke()` method of `Invoker` object which's returned from `refer()` call, the protocol
     * needs to correspondingly execute `invoke()` method of `Invoker` object <br>
     * 2. It's protocol's responsibility to implement `Invoker` which's returned from `refer()`. Generally speaking,
     * protocol sends remote request in the `Invoker` implementation. <br>
     * 3. When there's check=false set in URL, the implementation must not throw exception but try to recover when
     * connection fails.
     *
     * @param type Service class
     * @param url  URL address for the remote service
     * @return invoker service's local proxy
     */
    @Override
    public <T> Invoker<T> refer(Class<T> type, String url) {

        // 创建一个客户端
        Client client = this.createClient(url);
        return new DubboInvoker<>(type, url, client);
    }

    private Client createClient(String url) {
        // 这里我省略几步骤，因为我实在不知道中间那几步的目的是啥，我直接使用 NettyTransporter 创建 Netty 客户端了
        Transporter transporter = new NettyTransporter();
        return transporter.connect(url, requestHandler);
    }

    /**
     * Destroy protocol: <br>
     * 1. Cancel all services this protocol exports and refers <br>
     * 2. Release all occupied resources, for example: connection, port, etc. <br>
     * 3. Protocol can continue to export and refer new service even after it's destroyed.
     */
    @Override
    public void destroy() {

    }
}
