package cn.x5456.summer.rpc.config.summer;

import cn.x5456.summer.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcComponentScanRegistrar.class)
public @interface RpcComponentScan {

    /**
     * 扫描的包路径
     */
    String value();
}