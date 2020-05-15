package cn.x5456.summer.rpc.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 服务提供者全局配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ProviderConfig extends AbstractConfig {

    // filter 过滤器
    private String filter;
}