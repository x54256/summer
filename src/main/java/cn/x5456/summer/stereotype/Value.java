package cn.x5456.summer.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring 中还可以放在 ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE 上，但是我好想都没用过
 *
 * @author yujx
 * @date 2020/04/20 14:27
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {

    /**
     * el 表达式
     */
    String value();
}
