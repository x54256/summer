package cn.x5456.summer.rpc.transporter;

import cn.x5456.summer.rpc.Invocation;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yujx
 * @date 2020/05/29 10:42
 */
public interface ChannelHandler {

    /**
     * 当请求来的时候调用
     */
    void received(ChannelHandlerContext ctx, Invocation msg);
}
