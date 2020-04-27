package cn.x5456.summer.web.bind.annotation;

import cn.x5456.summer.beans.factory.annotation.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {

    // 切入的包
    String[] value() default {};
}