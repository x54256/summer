package rpc.test.provider;

import cn.x5456.summer.context.annotation.Configuration;
import cn.x5456.summer.context.annotation.PropertySource;
import cn.x5456.summer.rpc.config.summer.EnableRpcConfig;
import cn.x5456.summer.rpc.config.summer.RpcComponentScan;

/**
 * @author yujx
 * @date 2020/05/15 10:16
 */
@RpcComponentScan("cn.x5456.summer.rpc")
@EnableRpcConfig
@Configuration
@PropertySource("/Users/x5456/IdeaProjects/Summer/test-rpc/test-rpc-provider/src/main/resources/application.properties")
public class TestRpcConfig {
}
