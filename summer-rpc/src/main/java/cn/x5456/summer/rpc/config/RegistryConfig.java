package cn.x5456.summer.rpc.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 注册中心配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class RegistryConfig extends AbstractConfig {

    // 127.0.0.1:2181
    private String address;

    // zookeeper
    private String protocol;

}