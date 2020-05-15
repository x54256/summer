package cn.x5456.summer.rpc.test;

import cn.x5456.summer.context.annotation.Configuration;
import cn.x5456.summer.context.annotation.PropertySource;
import cn.x5456.summer.rpc.config.summer.EnableRpc;

/**
 * @author yujx
 * @date 2020/05/15 10:16
 */
@EnableRpc
@Configuration
@PropertySource("/Users/x5456/IdeaProjects/Summer/test-rpc/src/main/resources/application.properties")
public class TestRpcConfig {
}
