package cn.x5456.summer.context.properties;

import cn.x5456.summer.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/05/15 15:49
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ConfigurationPropertiesBindingPostProcessor.class)
public @interface EnableConfigurationProperties {
}
