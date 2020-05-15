package cn.x5456.summer.rpc.config.summer;

import cn.x5456.summer.context.annotation.Import;
import cn.x5456.summer.context.properties.EnableConfigurationProperties;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/05/15 09:29
 */
@Import({RpcConfiguration.class})
@EnableConfigurationProperties
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface EnableRpc {
}
