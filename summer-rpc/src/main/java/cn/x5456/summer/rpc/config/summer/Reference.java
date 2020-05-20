package cn.x5456.summer.rpc.config.summer;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/05/20 10:55
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Reference {
}
