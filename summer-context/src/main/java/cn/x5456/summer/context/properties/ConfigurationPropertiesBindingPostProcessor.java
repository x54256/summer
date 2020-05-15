package cn.x5456.summer.context.properties;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.x5456.summer.beans.factory.config.BeanDefinition;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.context.ApplicationContext;
import cn.x5456.summer.context.ApplicationContextAware;
import cn.x5456.summer.context.EnvironmentAware;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.core.util.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor, EnvironmentAware, ApplicationContextAware {

    private Environment environment;

    private ApplicationContext applicationContext;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    /**
     * 注：只支持基本类型
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        ConfigurationProperties configurationProperties = AnnotationUtil.getAnnotation(beanClass, ConfigurationProperties.class);

        // 如果没有在类上找到那么可能是工厂方法上的注解
        if (configurationProperties == null) {
            BeanDefinition bd = applicationContext.getBeanDefinition(beanName);
            if (StrUtil.isNotBlank(bd.getFactoryBean())) {
                String factoryBean = bd.getFactoryBean();
                String factoryMethod = bd.getFactoryMethod();

                Object fb = applicationContext.getBean(factoryBean);
                Method method = ReflectUtil.getMethod(fb.getClass(), factoryMethod);
                if (method != null) {
                    configurationProperties = AnnotationUtil.getAnnotation(method, ConfigurationProperties.class);
                }
            }
        }

        if (configurationProperties != null) {
            String prefix = configurationProperties.value();
            Map<String, String> propertyByPrefix = environment.getPropertyByPrefix(prefix);
            propertyByPrefix.forEach((fieldName, fieldValue) -> {
                Field field = ReflectUtil.getField(beanClass, fieldName);
                if (field != null) {
                    Class<?> type = field.getType();
                    ReflectUtil.setFieldValue(bean, field, ReflectUtils.string2BasicType(fieldValue, type));
                }
            });
        }

        return bean;
    }
}