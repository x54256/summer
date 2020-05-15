package cn.x5456.summer.rpc.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 协议配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ProtocolConfig extends AbstractConfig {

    private static final long serialVersionUID = 6913423882496634749L;

    // protocol name dubbo
    private String name;

    // service port  20880
    private Integer port;
}