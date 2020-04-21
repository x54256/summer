package cn.x5456.summer.beans.factory.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.factory.BeanFactory;
import cn.x5456.summer.beans.factory.BeanFactoryAware;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.core.env.PropertyResolver;

import java.lang.reflect.Field;

/**
 * 只处理 @Value 注解
 *
 * @author yujx
 * @date 2020/04/20 14:21
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 在bean的初始化后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        for (Field field : bean.getClass().getDeclaredFields()) {
            Value annotation = AnnotationUtil.getAnnotation(field, Value.class);
            if (ObjectUtil.isNotNull(annotation)) {

                Object fieldValue = annotation.value();
                String realValue = (String) fieldValue;
                if (realValue.startsWith("${")) {
                    realValue = realValue.substring(2, realValue.length() - 1);
                }

                for (PropertyResolver propertyResolver : beanFactory.getEmbeddedValueResolvers()) {
                    if (ObjectUtil.isNotNull(propertyResolver.getProperty(realValue))) {
                        fieldValue = propertyResolver.getProperty(realValue);
                        break;
                    }
                }

                ReflectUtil.setFieldValue(bean, field, fieldValue);
            }
        }

        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
