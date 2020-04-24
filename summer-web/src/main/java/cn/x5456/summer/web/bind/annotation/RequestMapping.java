package cn.x5456.summer.web.bind.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * 此注释表示的主要映射。
     */
    String[] value() default {};

    /**
     * 要映射到的HTTP请求方法，从而缩小了主要映射：
     * GET, POST, HEAD, OPTIONS, PUT, PATCH, DELETE, TRACE.
     */
    RequestMethod[] method() default {};
}