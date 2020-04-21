package cn.x5456.summer.context.annotation;

import cn.x5456.summer.beans.factory.annotation.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yujx
 * @date 2020/04/18 15:50
 */
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {

    String value() default "";
}
