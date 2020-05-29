package cn.x5456.summer.rpc.transporter;

/**
 * @author yujx
 * @date 2020/05/29 10:42
 */
public class NettyTransporter implements Transporter {

    /**
     * Bind a server.
     */
    @Override
    public Server bind(String url, ChannelHandler handler) {
        return new NettyServer(url, handler);
    }

    /**
     * Connect to a server.
     */
    @Override
    public Client connect(String url, ChannelHandler handler) {
        return new NettyClient(url, handler);
    }
}
