package cn.x5456.summer.beans.factory.annotation;

import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.core.util.ReflectUtils;

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
