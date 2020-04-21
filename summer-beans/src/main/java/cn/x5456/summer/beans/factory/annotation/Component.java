package cn.x5456.summer.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意：这个注解在 Spring 中是放在 context 包下的，ApplicationContext 向 BF 注册了一个 xml 解析器，
 * 当解析 component-scan 标签的时候获取出来进行的解析，但是为了方便，所以将这个注解放到了 beans 中。
 *
 * @author yujx
 * @date 2020/04/18 15:14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    String value() default "";
}
