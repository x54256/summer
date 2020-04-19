package cn.x5456.summer.stereotype;

import java.lang.annotation.*;

/**
 * Spring 中 @Conditional 注解可以放到方法上，但是我这里就不允许了
 * <p>
 * 注解到方法表示 @Bean 是否可用
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Conditional {

    /**
     * 条件上下文对应的接口
     *
     * @return 条件列表
     */
    Class<? extends Condition> value();

}
