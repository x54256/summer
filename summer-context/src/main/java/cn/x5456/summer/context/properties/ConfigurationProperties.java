package cn.x5456.summer.context.properties;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/05/15 15:49
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationProperties {

    String value();
}
