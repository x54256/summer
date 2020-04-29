package cn.x5456.summer.web.bind.annotation;

import cn.x5456.summer.http.HttpStatus;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseStatus {

    HttpStatus value() default HttpStatus.INTERNAL_SERVER_ERROR;
}