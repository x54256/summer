package cn.x5456.summer.rpc.transporter;

import cn.x5456.summer.core.util.JsonUtils;
import cn.x5456.summer.rpc.proxy.RpcInvocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.net.URI;

/**
 * @author yujx
 * @date 2020/05/29 11:15
 */
public class NettyHandler extends SimpleChannelInboundHandler<HttpObject> {

    private ChannelHandler handler;

    public NettyHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    // 当请求来的时候调用（服务提供者端）
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {

            HttpRequest httpRequest = (HttpRequest) msg;
            String query = new URI(httpRequest.uri()).getQuery();
            System.out.println(query);

            // 这里 dubbo 源码中使用 Request 对象进行了进一步封装，我们为了方便直接就转成 Invoker 对象
            String requestInfo = query.substring("query=".length());
            RpcInvocation invocation = JsonUtils.toBean(requestInfo, RpcInvocation.class);
            handler.received(ctx, invocation);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
