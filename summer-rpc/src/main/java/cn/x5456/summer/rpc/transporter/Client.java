package cn.x5456.summer.rpc.transporter;

import cn.x5456.summer.rpc.Invocation;

/**
 * @author yujx
 * @date 2020/05/29 10:42
 */
public interface Client {

    // dubbo 中是封装的是 request 对象
    void sent(Invocation invocation);
}
