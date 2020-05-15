package cn.x5456.summer.rpc.config.summer;

import cn.x5456.summer.context.annotation.Bean;
import cn.x5456.summer.context.properties.ConfigurationProperties;
import cn.x5456.summer.rpc.config.*;

/**
 * @author yujx
 * @date 2020/05/15 16:32
 */
public class RpcConfiguration {

    @Bean
    @ConfigurationProperties(value = "rpc.application")
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }

    @Bean
    @ConfigurationProperties(value = "rpc.consumer")
    public ConsumerConfig consumerConfig() {
        return new ConsumerConfig();
    }

    @Bean
    @ConfigurationProperties(value = "rpc.protocol")
    public ProtocolConfig protocolConfig() {
        return new ProtocolConfig();
    }

    @Bean
    @ConfigurationProperties(value = "rpc.provider")
    public ProviderConfig providerConfig() {
        return new ProviderConfig();
    }

    @Bean
    @ConfigurationProperties(value = "rpc.registry")
    public RegistryConfig registryConfig() {
        return new RegistryConfig();
    }
}
