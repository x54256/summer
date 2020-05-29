package cn.x5456.summer.rpc.transporter;

import cn.x5456.summer.rpc.Invocation;
import cn.x5456.summer.rpc.Result;

/**
 * @author yujx
 * @date 2020/05/29 10:42
 */
public interface Client {

    // dubbo 中是封装的是 request 和 response 对象
    Result sent(Invocation invocation);
}
