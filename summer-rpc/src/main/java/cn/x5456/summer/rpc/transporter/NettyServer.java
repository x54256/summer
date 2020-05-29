package cn.x5456.summer.rpc.transporter;

import cn.hutool.core.util.ReUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author yujx
 * @date 2020/05/29 10:44
 */
public class NettyServer implements Server {


    public NettyServer(final String url, final ChannelHandler handler) {


        Runnable runnable = new Runnable() {
            public void run() {
                // 创建两个事件循环组
                NioEventLoopGroup bossGroup = new NioEventLoopGroup();  // 获取连接，连接获取到之后，交给下面的去处理
                NioEventLoopGroup workerGroup = new NioEventLoopGroup();

                try {
                    // 简化服务端启动
                    ServerBootstrap serverBootstrap = new ServerBootstrap();
                    // 定义子处理器，当连接创建之后会自动调用初始化器
                    serverBootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    ChannelPipeline pipeline = socketChannel.pipeline();

                                    // 解码器（将2进制的数据解析成真正携带的东西）
                                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                                    // 编码器
                                    pipeline.addLast(new LengthFieldPrepender(4));
                                    // 字符集编解/码器
                                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));

                                    // netty 我不会，我只能大概模拟一下
                                    pipeline.addLast(new NettyHandler(handler));
                                }
                            }); // 子处理器（对请求进行处理）

                    // 根据 url 获取端口号
                    String port = ReUtil.getGroup0("bind.port=\\d+&", url).replace("bind.port=", "").replace("&", "");

                    // 绑定端口号
                    ChannelFuture channelFuture = serverBootstrap.bind(Integer.parseInt(port)).sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 采用优雅关闭
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        };

        new Thread(runnable).start();
    }
}
