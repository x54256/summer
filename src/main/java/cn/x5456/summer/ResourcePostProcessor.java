package cn.x5456.summer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.factory.BeanFactory;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @author yujx
 * @date 2020/04/19 15:51
 */
public class ResourcePostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 后置处理注入属性
     */
    @Override
    public void postProcessPropertyValues(Object bean, String beanName) {
        for (Field field : bean.getClass().getDeclaredFields()) {
            Resource annotation = AnnotationUtil.getAnnotation(field, Resource.class);
            if (ObjectUtil.isNotNull(annotation)) {
                Object fieldRef = beanFactory.getBean(field.getName());
                ReflectUtil.setFieldValue(bean, field, fieldRef);
            }
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
