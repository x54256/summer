package cn.x5456.summer.rpc.transporter;

import cn.x5456.summer.core.util.JsonUtils;
import cn.x5456.summer.rpc.proxy.RpcInvocation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yujx
 * @date 2020/05/29 11:15
 */
public class NettyHandler extends SimpleChannelInboundHandler<String> {

    private ChannelHandler handler;

    public NettyHandler(ChannelHandler handler) {
        this.handler = handler;
    }

    // 当请求来的时候调用（服务提供者端）
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 打印客户端的 ip:port
        System.out.println(ctx.channel().remoteAddress() + "," + msg);

        // 这里 dubbo 源码中使用 Request 对象进行了进一步封装，我们为了方便直接就转成 Invoker 对象
        RpcInvocation invocation = JsonUtils.toBean(msg, RpcInvocation.class);
        handler.received(ctx, invocation);
    }
}
