package cn.x5456.summer.stereotype;

import cn.x5456.summer.beans.factory.annotation.Component;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/04/22 16:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Controller {

    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String value() default "";

}

