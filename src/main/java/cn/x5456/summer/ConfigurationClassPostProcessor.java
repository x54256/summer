package cn.x5456.summer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.x5456.summer.beans.BeanDefinition;
import cn.x5456.summer.beans.DefaultBeanDefinition;
import cn.x5456.summer.beans.factory.BeanDefinitionRegistry;
import cn.x5456.summer.beans.factory.ListableBeanFactory;
import cn.x5456.summer.stereotype.Bean;
import cn.x5456.summer.stereotype.Configuration;
import cn.x5456.summer.util.ReflectUtils;

import java.lang.reflect.Method;

/**
 * @author yujx
 * @date 2020/04/18 17:37
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        ListableBeanFactory bf = (ListableBeanFactory) registry;

        for (String bdName : bf.getBeanDefinitionNames()) {
            BeanDefinition classBeanDefinition = registry.getBeanDefinition(bdName);
            String className = classBeanDefinition.getClassName();
            Class<?> clazz = ReflectUtils.getType(className);

            // 找到类上携带 @Configuration 的
            Configuration configuration = AnnotationUtil.getAnnotation(clazz, Configuration.class);
            if (ObjectUtil.isNotEmpty(configuration)) {
                // 循环找方法中包含 @Bean 注解的
                for (Method method : clazz.getMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        BeanDefinition bdDef = new DefaultBeanDefinition();

                        Bean bean = method.getAnnotation(Bean.class);
                        String beanName = ObjectUtil.isNotEmpty(bean.value()) ? bean.value() : method.getName();

                        bdDef.setName(beanName);
                        bdDef.setFactoryBean(classBeanDefinition.getName());
                        bdDef.setFactoryMethod(method.getName());
                        bdDef.setInitMethod(bean.initMethod());
                        bdDef.setDestroyMethod(bean.destroyMethod());

                        // TODO: 2020/4/18 属性列表

                        registry.registerBeanDefinition(beanName, bdDef);
                    }
                }
            }
        }
    }
}
