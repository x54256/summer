package cn.x5456.summer.rpc.config.summer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.beans.factory.support.DefaultBeanDefinition;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.rpc.config.ReferenceBean;

import java.lang.reflect.Field;

/**
 * 功能：创建 ReferenceBean 的 bd，注入 Spring 容器中进行依赖注入等，然后获取他的 bean 实例，进行 DI
 *
 * @author yujx
 * @date 2020/05/20 10:07
 */
public class ReferenceAnnotationBeanPostProcessor implements ApplicationContextAware, BeanPostProcessor {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        // 找出 bean 中所有携带 @Reference 注解的字段（方法就不找了）
        for (Field field : ReflectUtil.getFields(bean.getClass())) {
            Reference reference = AnnotationUtil.getAnnotation(field, Reference.class);
            if (reference != null) {
                // 从容器中找一下是否有这个 bean
                String fieldName = field.getName();
                Object refBean = applicationContext.getBean(fieldName);

                // 没有则创建一个 ReferenceBean 将其注入容器中
                if (refBean == null) {
                    DefaultBeanDefinition bdDef = new DefaultBeanDefinition();
                    bdDef.setName(fieldName);
                    bdDef.setClassName(ReferenceBean.class.getName());
                    bdDef.addProperty("interfaceClass", String.class.getName(), field.getDeclaringClass().getName(), null);

                    applicationContext.registerBeanDefinition(fieldName, bdDef);
                    refBean = applicationContext.getBean(fieldName);
                }

                // 然后获取出来，进行 DI
                ReflectUtil.setFieldValue(bean, field, refBean);
            }
        }

        return bean;
    }
}
