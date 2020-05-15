package cn.x5456.summer.context.properties;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.x5456.summer.beans.factory.config.BeanPostProcessor;
import cn.x5456.summer.context.EnvironmentAware;
import cn.x5456.summer.core.env.Environment;
import cn.x5456.summer.core.util.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class ConfigurationPropertiesBindingPostProcessor implements BeanPostProcessor, EnvironmentAware {

    private Environment environment;

	@Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

	/**
	 * 注：只支持基本类型
	 */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        ConfigurationProperties configurationProperties = AnnotationUtil.getAnnotation(beanClass, ConfigurationProperties.class);
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