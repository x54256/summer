package cn.x5456.summer.rpc.transporter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author yujx
 * @date 2020/05/29 15:24
 */
public class NettyHttpServer implements Server {

    public NettyHttpServer(final String url, final ChannelHandler handler) {

        Runnable runnable = new Runnable() {
            public void run() {
                // 创建两个事件循环组
                NioEventLoopGroup bossGroup = new NioEventLoopGroup();
                NioEventLoopGroup workerGroup = new NioEventLoopGroup();

                try {
                    // 服务端启动类（助手类，它简化了服务端创建的工作）
                    ServerBootstrap serverBootstrap = new ServerBootstrap();
                    // 定义子处理器，当连接创建之后会自动调用初始化器
                    serverBootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer() {
                                @Override
                                protected void initChannel(Channel ch) throws Exception {
                                    // 管道，管道中可以有多个处理器
                                    ChannelPipeline pipeline = ch.pipeline();

                                    // 增加到最后
                                    pipeline.addLast("httpServerCodec", new HttpServerCodec());  // HttpServerCodec：将请求进行编解码（不能使用单例）
                                    // 将我们规格写的处理器，添加到pipline
                                    pipeline.addLast("testHttpServerHandler", new NettyHandler(handler));
                                }
                            });

                    // 绑定端口号
                    //  - sync：同步
                    // 继承java.util.concurrent.Future<V>，当运行之后，立刻返回一个future对象
                    // 可以通过future对象，判断是否执行完毕
                    ChannelFuture channelFuture = serverBootstrap.bind(20880).sync();

                    // 关闭netty
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 优雅关闭（处理完当前的线程，不在接收新的线程）
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        };

        new Thread(runnable).start();

    }
}
