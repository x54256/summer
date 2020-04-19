package cn.x5456.summer.stereotype;

import java.lang.annotation.*;

/**
 * @author yujx
 * @date 2020/04/19 10:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

    /**
     * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
     * 或者普通的组件（Component）需要引入的
     */
    Class<?>[] value();
}
