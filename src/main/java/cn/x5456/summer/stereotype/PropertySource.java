package cn.x5456.summer.stereotype;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// @Repeatable(PropertySources.class) 只带大家实现 @PropertySource 注解，@PropertySources 大家可以自行实现
public @interface PropertySource {

    /**
     * 文件路径列表
     */
    String[] value();
}