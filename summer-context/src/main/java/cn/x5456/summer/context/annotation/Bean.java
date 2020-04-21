package cn.x5456.summer.context.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Bean {

    /**
     * 组件名称
     */
    String value() default "";

    /**
     * 初始化方法
     */
    String initMethod() default "";

    /**
     * 销毁方法
     */
    String destroyMethod() default "";

}
