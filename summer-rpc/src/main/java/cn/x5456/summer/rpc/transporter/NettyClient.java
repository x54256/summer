package cn.x5456.summer.rpc.transporter;

import cn.x5456.summer.rpc.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 我不会 QAQ
 *
 * @author yujx
 * @date 2020/05/29 14:38
 */
public class NettyClient implements Client {

    // 构造的时候先与服务端建立连接
    public NettyClient(String url, ChannelHandler handler) {

        // 暂时写死，理论是从 url 中获取
        String ip = "127.0.0.1";
        int port = 20880;

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class) // 反射创建NioSocketChannel对象
                    .handler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();

                            // 解码器（将2进制的数据解析成真正携带的东西）
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            // 编码器
                            pipeline.addLast(new LengthFieldPrepender(4));
                            // 字符集编解/码器
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            // 我们自己的处理器
                            pipeline.addLast(new NettyHandler(handler));
                        }
                    });

            // 连接服务端
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

    // 方法调用的时候再发请求
    @Override
    public void sent(Invocation invocation) {


    }
}
