package cn.x5456.summer.rpc.config.summer;

import cn.x5456.summer.context.annotation.Bean;
import cn.x5456.summer.rpc.protocol.DubboProtocol;
import cn.x5456.summer.rpc.protocol.InjvmProtocol;

/**
 * @author yujx
 * @date 2020/05/20 09:19
 */
public class ProtocolConfiguration {

    private static final String suffix = "Protocol";

    @Bean(value = "injvm" + suffix)
    public InjvmProtocol injvmProtocol() {
        return new InjvmProtocol();
    }


    @Bean(value = "dubbo" + suffix)
    public DubboProtocol dubboProtocol() {
        return new DubboProtocol();
    }

}
