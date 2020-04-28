package cn.x5456.summer.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yujx
 * @date 2020/04/27 17:23
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {

    String value();

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}


