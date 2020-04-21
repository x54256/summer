package cn.x5456.summer.context.annotation;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/04/20 16:20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(ProfileCondition.class)
public @interface Profile {

    /**
     * 当前配置应用于哪些环境
     */
    String[] value();
}
