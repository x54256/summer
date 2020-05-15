package cn.x5456.summer.rpc.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 应用配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ApplicationConfig extends AbstractConfig {

    // 应用名
    private String name;

}