package cn.x5456.summer;

import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.util.ReflectUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * 此处只实现对 @PostConstruct 注解的处理，@PreDestroy 不实现（Spring 中通过 DestructionAwareBeanPostProcessor 实现）
 *
 * @author yujx
 * @date 2020/04/17 13:26
 */
public class InitAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        for (Method method : ReflectUtils.getMethodsByAnnotation(bean.getClass(), PostConstruct.class)) {
            ReflectUtil.invoke(bean, method);
        }
        return bean;
    }
}
