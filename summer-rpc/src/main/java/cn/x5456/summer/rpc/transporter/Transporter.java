package cn.x5456.summer.rpc.transporter;

/**
 * @author yujx
 * @date 2020/05/29 10:37
 */
public interface Transporter {

    /**
     * Bind a server.
     */
    Server bind(String url, ChannelHandler handler);

    /**
     * Connect to a server.
     */
    Client connect(String url, ChannelHandler handler);
}
